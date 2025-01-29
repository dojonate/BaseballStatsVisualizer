package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.*;
import com.dojonate.statsvisualizer.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;

@Component
public class EventFileParser {

    private static final Logger logger = LoggerFactory.getLogger(EventFileParser.class);
    @Autowired
    private final PlayerService playerService;

    @Autowired
    private final TeamService teamService;

//    @Autowired
//    private final GameService gameService;
//
//    @Autowired
//    private final PlayerEventService playerEventService;
//
//    @Autowired
//    private final RosterEntryService rosterEntryService;

    EventFileParser(TeamService teamService, PlayerService playerService, GameService gameService, PlayerEventService playerEventService, RosterEntryService rosterEntryService) {
        this.teamService = teamService;
        this.playerService = playerService;
//        this.gameService = gameService;
//        this.playerEventService = playerEventService;
//        this.rosterEntryService = rosterEntryService;
    }

    public Game parse(String content) {
        if (!isValidEventFile(content)) {
            throw new IllegalArgumentException("Invalid event file.");
        }

        String[] lines = content.split("\n");
        Game game = new Game();
        List<PlayerEvent> playerEvents = new ArrayList<>();
        Map<String, Player> players = new HashMap<>();
        Map<String, Team> teams = new HashMap<>();

        for (String line : lines) {
            String[] parts = line.split(",", -1);
            switch (parts[0]) {
                case "id":
                    game.setId(parts[1]);
                    break;
                case "info":
                    handleInfo(game, parts);
                    break;
                case "start":
                case "sub":
                    handlePlayer(parts, players, teams);
                    break;
                case "play":
                    PlayerEvent playerEvent = handlePlay(parts, players, game);
                    playerEvents.add(playerEvent);
                    break;
                case "com":
                    // Handle comments if necessary
                    break;
                case "data":
                    // Handle additional data if necessary
                    break;
                default:
                    // Handle other types if necessary
                    break;
            }
        }

        game.setPlayerEvents(playerEvents);
        return game;
    }

    private boolean isValidEventFile(String content) {
        // Basic checks for essential records
        if (!content.startsWith("id") || !content.contains("play")) {
            logger.warn("Event file validation failed: Missing essential records.");
            return false;
        }

        // Additional validation (e.g., correct format)
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
            case "id" -> parts.length == 2 && parts[1].substring(3).matches("\\d+"); // e.g., id,ATL198304080
            case "info" -> parts.length >= 3; // e.g., info,visteam,SDN
            case "play" -> parts.length >= 7; // e.g., play,5,1,playerId,00,,S8.3-H;1-2
            case "start", "sub" -> parts.length >= 6; // e.g., start,playerId,"Name",0,1,7
            case "badj", "radj", "padj", "com", "data" -> true; // Flexible record types
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
                game.setSite(parts[2]);
                break;
            case "date":
                game.setDate(parts[2]);
                break;
            case "number":
                game.setNumber(parts[2]);
                break;
            case "starttime":
                game.setStarttime(parts[2]);
                break;
            case "daynight":
                game.setDaynight(parts[2]);
                break;
            case "usedh":
                game.setUsedh(Boolean.parseBoolean(parts[2]));
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
            case "howscored":
                game.setHowscored(parts[2]);
                break;
            case "pitches":
                game.setPitches(parts[2]);
                break;
            case "oscorer":
                game.setOscorer(parts[2]);
                break;
            case "temp":
                game.setTemp(Integer.parseInt(parts[2]));
                break;
            case "winddir":
                game.setWinddir(parts[2]);
                break;
            case "windspeed":
                game.setWindspeed(Integer.parseInt(parts[2]));
                break;
            case "fieldcond":
                game.setFieldcond(parts[2]);
                break;
            case "precip":
                game.setPrecip(parts[2]);
                break;
            case "sky":
                game.setSky(parts[2]);
                break;
            case "timeofgame":
                game.setTimeofgame(Integer.parseInt(parts[2]));
                break;
            case "attendance":
                game.setAttendance(Integer.parseInt(parts[2]));
                break;
            case "wp":
                game.setWp(parts[2]);
                break;
            case "lp":
                game.setLp(parts[2]);
                break;
            case "save":
                game.setSave(parts[2]);
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
                newPlayer = playerService.save(new Player(playerId, firstName, lastName, "", "", LocalDate.of(1900, 1, 1)));
            }
            return newPlayer;
        });

        Team team = teams.computeIfAbsent(teamId, id -> teamService.findOrCreateTeam(id, "", ""));
    }

    private PlayerEvent handlePlay(String[] parts, Map<String, Player> players, Game game) {
        try {
            int inning = Integer.parseInt(parts[1]);
            Team battingTeam = "0".equals(parts[2]) ? game.getAwayTeam() : game.getHomeTeam();
            Team fieldingTeam = "0".equals(parts[2]) ? game.getHomeTeam() : game.getAwayTeam();
            String playerId = parts[3];
            String countCumulative = parts[4];
            String pitches = parts[5];
            String event = parts[6];

            Player player = players.get(playerId);

            int balls = Character.getNumericValue(countCumulative.charAt(0)),
                    strikes = Character.getNumericValue(countCumulative.charAt(1));


            // Parse the count field for pitch and events
            List<PitchType> pitchDetails = parsePitchDetails(pitches);

            // Parse the event field for details
            EventDetails eventDetails = parseEventDetails(event);

            // Create the PlayerEvent object
            PlayerEvent playerEvent = new PlayerEvent();
            playerEvent.setGame(game);
            playerEvent.setBatter(player);
            playerEvent.setBattingTeam(battingTeam);
            playerEvent.setFieldingTeam(fieldingTeam);
            playerEvent.setInning(inning);
            playerEvent.setCount(balls, strikes);
            playerEvent.setPitchList(pitchDetails);
            playerEvent.setEventType(eventDetails.getEventType());
            playerEvent.setDescription(eventDetails.getDescription());
            playerEvent.setRunnerAdvances(eventDetails.getRunnerAdvances());

            return playerEvent;
        } catch (Exception e) {
            logger.error("Failed to parse play record: {}", Arrays.toString(parts), e);
            throw e; // Rethrow for tests
        }
    }

    private List<PitchType> parsePitchDetails(String pitches) {
        List<PitchType> details = new ArrayList<>();
        for (Character c : pitches.toCharArray()) {
            PitchType pitchCount = PitchType.fromAbbreviation(c);
            if (pitchCount != null) {
                details.add(pitchCount);
            } else {
                throw new IllegalArgumentException("Invalid pitch type: " + c);
            }
        }
        return details;
    }

    private EventDetails parseEventDetails(String event) {
        return new EventDetails(event);
    }

    private String buildDescription(EventDetails details) {
        StringBuilder description = new StringBuilder();
        description.append("Event: ").append(details.getEventType());
        if (!details.getRunnerAdvances().isEmpty()) {
            description.append(", Runner Advances: ");
            for (RunnerAdvance advance : details.getRunnerAdvances()) {
                description.append(advance.getBaseMovement());
                if (advance.getDetails() != null) {
                    description.append(" (").append(advance.getDetails()).append(")");
                }
                description.append("; ");
            }
        }
        return description.toString().trim();
    }
}