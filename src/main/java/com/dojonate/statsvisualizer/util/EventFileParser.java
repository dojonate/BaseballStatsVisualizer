package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.*;
import com.dojonate.statsvisualizer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Component
public class EventFileParser {

    private static final Logger logger = LoggerFactory.getLogger(EventFileParser.class);

    @Autowired
    private final PlayerService playerService;
    @Autowired
    private final TeamService teamService;
    private final SiteService siteService;
    String gameDate;

    public EventFileParser(TeamService teamService, PlayerService playerService, SiteService siteService, GameService gameService, PlayerEventService playerEventService, RosterEntryService rosterEntryService) {
        this.teamService = teamService;
        this.playerService = playerService;
        this.siteService = siteService;
    }

    /**
     * Parse a file that may contain multiple games.
     * Each game is assumed to start with an "id" record.
     * Returns a list of Game objects.
     */
    public List<Game> parseMultiple(String content) {
        if (!isValidEventFile(content)) {
            throw new IllegalArgumentException("Invalid event file.");
        }

        List<Game> games = new ArrayList<>();
        String[] lines = content.split("\n");

        // Variables to hold state for the current game being processed
        Game currentGame = null;
        List<PlayerEvent> currentPlayerEvents = new ArrayList<>();
        Map<String, Player> players = new HashMap<>();
        Map<String, Team> teams = new HashMap<>();
        GameState state = new GameState();
        gameDate = null;

        for (String line : lines) {
            String[] parts = line.split(",", -1);
            switch (parts[0]) {
                case "id":
                    // When a new game starts, finalize the previous one (if any)
                    if (currentGame != null) {
                        currentGame.setPlayerEvents(currentPlayerEvents);
                        games.add(currentGame);
                    }
                    // Reset state for new game
                    currentGame = new Game();
                    currentPlayerEvents = new ArrayList<>();
                    players = new HashMap<>();
                    teams = new HashMap<>();
                    state = new GameState();
                    currentGame.setId(parts[1]);
                    break;
                case "info":
                    handleInfo(currentGame, parts);
                    if ("date".equals(parts[1])) {
                        gameDate = parts[2];
                    }
                    break;
                case "start":
                case "sub":
                    handlePlayer(parts, players, teams);
                    break;
                case "play":
                    try {
                        PlayerEvent playerEvent = handlePlay(parts, players, currentGame, state);
                        currentPlayerEvents.add(playerEvent);
                    } catch (Exception e) {
                        logger.error("Failed to parse play record: {}", Arrays.toString(parts), e);
                        throw e;
                    }
                    break;
                case "com":
                    // Optionally handle comments here
                    break;
                case "data":
                    // Optionally handle additional data here
                    break;
                default:
                    // Log unknown record types
                    logger.warn("Unknown record type: {}", parts[0]);
                    break;
            }
        }
        // Add the last game if present
        if (currentGame != null) {
            currentGame.setPlayerEvents(currentPlayerEvents);
            games.add(currentGame);
        }
        return games;
    }

    private boolean isValidEventFile(String content) {
        // Basic checks for essential records
        if (!content.startsWith("id") || !content.contains("play")) {
            logger.warn("Event file validation failed: Missing essential records.");
            return false;
        }
        // Additional validation
        String[] lines = content.split("\n");
        for (String line : lines) {
            if (!isValidRecord(line)) {
                logger.warn("Event file validation failed: Invalid record found - {}", line);
                return false;
            }
        }
        return true;
    }

    private boolean isValidRecord(String line) {
        String[] parts = line.split(",", -1);
        return switch (parts[0]) {
            case "id" -> parts.length == 2 && parts[1].length() > 4 && parts[1].substring(3).matches("\\d+");
            case "info" -> parts.length >= 3;
            case "play" -> parts.length >= 7;
            case "start", "sub" -> parts.length >= 6;
            case "badj", "radj", "padj", "com", "data", "version" -> true;
            default -> {
                logger.warn("Unknown record type: {}", parts[0]);
                yield false;
            }
        };
    }

    private void handleInfo(Game game, String[] parts) {
        switch (parts[1]) {
            case "visteam":
                game.setAwayTeam(teamService.findOrCreateTeam(parts[2], "", ""));
                break;
            case "hometeam":
                game.setHomeTeam(teamService.findOrCreateTeam(parts[2], "", ""));
                break;
            case "site":
                game.setSite(siteService.findById(parts[2]));
                break;
            case "date":
                gameDate = parts[2];
                break;
            case "number":
                Integer gameNumber = Integer.parseInt(parts[2]);
                if (!gameNumber.equals(0)) {
                    game.setId(game.getId() + "-" + gameNumber);
                }
                game.setGameNumber(gameNumber);
                break;
            case "starttime":
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd h:mma");
                TemporalAccessor dateOfGame = formatter.parse(gameDate + " " + parts[2]);
                game.setStartTime(parts[2]);
                break;
            case "daynight":
                game.setNightGame("night".equals(parts[2]));
                break;
            case "usedh":
                game.setUseDesignatedHitter(Boolean.parseBoolean(parts[2]));
                break;
            case "umphome":
                game.setUmphome(parts[2]);
                break;
            case "ump1b":
                game.setUmp1b(parts[2]);
                break;
            case "ump2b":
                game.setUmp2b(parts[2]);
                break;
            case "ump3b":
                game.setUmp3b(parts[2]);
                break;
            case "pitches":
                game.setPitches(parts[2]);
                if (!"pitches".equals(parts[2])) {
                    logger.warn("Pitch-by-pitch not recorded");
                }
                break;
            case "oscorer":
                game.setOfficialScorer(parts[2]);
                break;
            case "temp":
                game.setTemperature(Integer.parseInt(parts[2]));
                break;
            case "winddir":
                game.setWindDirection(WindDirection.fromString(parts[2]));
                break;
            case "windspeed":
                game.setWindSpeed(Integer.parseInt(parts[2]));
                break;
            case "fieldcond":
                game.setFieldConditions(FieldConditions.fromDescription(parts[2]));
                break;
            case "precip":
                game.setPrecipitation(PrecipitationType.fromString(parts[2]));
                break;
            case "sky":
                game.setSky(SkyType.fromString(parts[2]));
                break;
            case "timeofgame":
                game.setLengthOfGame(Integer.parseInt(parts[2]));
                break;
            case "attendance":
                game.setAttendance(Integer.parseInt(parts[2]));
                break;
            case "wp":
                game.setWp(playerService.findById(parts[2]));
                break;
            case "lp":
                game.setLp(playerService.findById(parts[2]));
                break;
            case "save":
                game.setSave(playerService.findById(parts[2]));
                break;
            default:
                // Handle other info types if necessary
                break;
        }
    }

    private void handlePlayer(String[] parts, Map<String, Player> players, Map<String, Team> teams) {
        String playerId = parts[1];
        String playerName = parts[2];
        String teamId = parts[3];
        int battingOrder = Integer.parseInt(parts[4]);
        int position = Integer.parseInt(parts[5]);

        Player player = players.computeIfAbsent(playerId, id -> {
            String[] nameParts = playerName.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts[1];
            Player newPlayer = playerService.findById(playerId);
            if (newPlayer == null) {
                newPlayer = playerService.save(new Player(playerId, firstName, lastName, "", "", java.time.LocalDate.of(1900, 1, 1)));
            }
            return newPlayer;
        });

        teams.computeIfAbsent(teamId, id -> teamService.findOrCreateTeam(id, "", ""));
    }

    private PlayerEvent handlePlay(String[] parts, Map<String, Player> players, Game game, GameState state) {
        try {
            int inning = Integer.parseInt(parts[1]);
            Team battingTeam = "0".equals(parts[2]) ? game.getAwayTeam() : game.getHomeTeam();
            Team fieldingTeam = "0".equals(parts[2]) ? game.getHomeTeam() : game.getAwayTeam();
            String playerId = parts[3];
            String countCumulative = parts[4];
            String pitches = parts[5];
            String event = parts[6];

            Player player = players.get(playerId);
            int balls = Character.getNumericValue(countCumulative.charAt(0));
            int strikes = Character.getNumericValue(countCumulative.charAt(1));


            // Parse the count field for pitch and events
            List<PitchType> pitchDetails = parsePitchDetails(pitches);
            state.setBatter(player);

            // Parse the event field for details
            EventDetails eventDetails = new EventDetails(event, state);


            // Create the PlayerEvent object
            PlayerEvent playerEvent = new PlayerEvent();
            playerEvent.setGame(game);
            playerEvent.setBatter(player);
            playerEvent.setBattingTeam(battingTeam);
            playerEvent.setFieldingTeam(fieldingTeam);
            playerEvent.setInning(inning);
            playerEvent.setCount(balls, strikes);
            playerEvent.setPitchList(pitchDetails);
            playerEvent.setEventType(eventDetails.getEventTypes());
            playerEvent.setDescription(eventDetails.getDescription());
            playerEvent.setRunnerAdvances(eventDetails.getRunnerAdvances());

            return playerEvent;
        } catch (Exception e) {
            logger.error("Failed to parse play record: {}", Arrays.toString(parts), e);
            throw e;
        }
    }

    private List<PitchType> parsePitchDetails(String pitches) {
        List<PitchType> details = new ArrayList<>();
        for (Character c : pitches.toCharArray()) {
            PitchType pitchCount = PitchType.fromAbbreviation(c.toString());
            if (c.equals('+')) {
                details.set(details.size() - 1, PitchType.fromAbbreviation(details.get(details.size() - 1).getAbbreviation() + "+"));
            } else if (pitchCount != null) {
                details.add(pitchCount);
            } else {
                throw new IllegalArgumentException("Invalid pitch type: " + c);
            }
        }
        return details;
    }

    // The original parse(String content) method could delegate to parseMultiple() and return the first game if desired:
    public Game parse(String content) {
        List<Game> games = parseMultiple(content);
        if (games.isEmpty()) {
            throw new IllegalArgumentException("No valid games found in the event file.");
        }
        return games.get(0); // or handle as appropriate
    }
}
