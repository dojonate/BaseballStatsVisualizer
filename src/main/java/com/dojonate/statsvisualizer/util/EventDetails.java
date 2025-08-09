package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.EventType;
import com.dojonate.statsvisualizer.model.Player;
import com.dojonate.statsvisualizer.model.Position;
import com.dojonate.statsvisualizer.model.RunnerAdvance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.getInteger;
import static java.lang.Integer.parseInt;

public class EventDetails {
    private static final Logger logger = LoggerFactory.getLogger(EventDetails.class);

    private final List<EventType> eventTypes;
    private String description;
    private List<RunnerAdvance> runnerAdvances; // e.g., "1-2", "2-H" with extra parameters
    private List<Position> playSequence;
    private String hitLocation;
    private String relayLocation;

    public EventDetails() {
        this.eventTypes = new ArrayList<>();
        this.runnerAdvances = new ArrayList<>();
        this.playSequence = new ArrayList<>();
    }

    public EventDetails(String event, GameState state) {
        this();
        parseEventDetails(event, state);
    }

    // Getters and setters...

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(EventType eventTypes) {
        this.eventTypes.add(eventTypes);
    }

    public void setEventType(List<EventType> eventType) {
        this.eventTypes.addAll(eventType);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RunnerAdvance> getRunnerAdvances() {
        return runnerAdvances;
    }

    public void setRunnerAdvances(List<RunnerAdvance> runnerAdvances) {
        this.runnerAdvances = runnerAdvances;
    }

    public List<Position> getPlaySequence() {
        return playSequence;
    }

    public void setPlaySequence(List<Position> playSequence) {
        this.playSequence = playSequence;
    }

    private void parseEventDetails(String event, GameState state) {
        // Split the event into two parts: before the period and after.
        List<String> eventParts = new ArrayList<>(List.of(event.split("\\.", 2)));

        // Process primary event info (before the period)
        String primaryPart = eventParts.get(0).trim();
        String[] tokens = primaryPart.split("/");
        for (String token : tokens) {
            token = token.trim();

            // Special case: if the token is in the form "F9LF" (letters, digits, letters)
            // then extract the initial letter(s) as the event code and ignore the rest.
            if (token.matches("^[A-Za-z]+\\d+[A-Za-z]+$")) {
                int firstDigitIndex = -1;
                for (int i = 0; i < token.length(); i++) {
                    if (Character.isDigit(token.charAt(i))) {
                        firstDigitIndex = i;
                        break;
                    }
                }
                if (firstDigitIndex > 0) {
                    String eventCode = token.substring(0, firstDigitIndex);
                    eventTypes.add(EventType.fromAbbreviation(eventCode));

                    String hitLocation = token.substring(firstDigitIndex);
                    if (hitLocation.length() > 1) {
                        this.hitLocation = hitLocation.substring(1);
                    }
                }
                continue; // Skip the remainder of the loop for this token.
            }

            // Existing logic for tokens with "+" symbols.
            if (token.contains("+")) {
                String[] subTokens = token.split("\\+");
                for (String sub : subTokens) {
                    if (!sub.isEmpty() && !sub.contains("SB")) {
                        eventTypes.add(EventType.fromAbbreviation(sub));
                    } else {
                        eventTypes.add(EventType.STOLEN_BASE);
                        String baseStolen = sub.replaceAll("\\D", "");
                        int base = parseInt(baseStolen);
                        eventParts.add(base - 1 + "-" + base);
                    }
                }
            } else if (token.contains("-")) {
                String cleaned = token.replace("-", "");
                eventTypes.add(EventType.fromAbbreviation(cleaned));
            } else if (token.matches(".*\\d.*")) {
                // Remove any parenthesized groups so that digits inside them aren’t interpreted as positions.
                String tokenNoParams = token.replaceAll("\\([^)]*\\)", "").trim();
                // Now extract letters and digits separately.
                List<String> eventPartTokens = List.of(tokenNoParams.replaceAll("[^a-zA-Z]", "/").split("/"));
                String positionsToken = tokenNoParams.replaceAll("[^\\d]", "");
                if (!positionsToken.isEmpty() && eventPartTokens.isEmpty()) {
                    // For example, a token like "23" means add positions for runner on 2 then 3.
                    for (char ch : positionsToken.toCharArray()) {
                        int index = Character.getNumericValue(ch);
                        Position pos = Position.fromPositionNumber(index);
                        if (pos != null && (playSequence.isEmpty() || pos != playSequence.get(playSequence.size() - 1))) {
                            playSequence.add(pos);
                        }
                    }
                } else {
                    for (String eventPartToken : eventPartTokens) {
                        if (eventPartToken.equalsIgnoreCase("HR")) {
                            eventTypes.add(EventType.INSIDE_THE_PARK_HOME_RUN);
                        } else {
                            EventType eventType = EventType.fromAbbreviation(eventPartToken);
                            eventTypes.add(eventType);
                            if (eventType == EventType.RELAY_THROW) {
                                relayLocation = positionsToken;
                            }
                        }
                    }
                }
            } else {
                eventTypes.add(EventType.fromAbbreviation(token));
            }
        }

        // Process runner advances (if any)
        if (eventParts.size() > 1) {
            String runnerData = eventParts.get(1).trim();
            // (Assuming validateRunnerAdvances(runnerData) throws an exception on invalid data.)
            validateRunnerAdvances(runnerData);
            List<RunnerAdvance> advances = parseRunnerAdvances(runnerData, state);
            setRunnerAdvances(advances);
        }

        // Build a human–readable description from the event type, play sequence, and runner advances.
        setDescription(buildDescription());
    }


    private List<RunnerAdvance> parseRunnerAdvances(String runnerData, GameState state) {
        List<RunnerAdvance> advances = new ArrayList<>();
        // Split runner advances on semicolon.
        String[] tokens = runnerData.split(";");

        // Build an ordered list of base runners – per Retrosheet the non-batter advances apply in the order:
        // runner on third first, then second, then first.
        List<Player> nonBatterRunners = new ArrayList<>();
        if (state.getThirdBase() != null) nonBatterRunners.add(state.getThirdBase());
        if (state.getSecondBase() != null) nonBatterRunners.add(state.getSecondBase());
        if (state.getFirstBase() != null) nonBatterRunners.add(state.getFirstBase());
        int nonBatterIndex = 0;

        // Pattern to capture any parenthesized groups.
        Pattern paramPattern = Pattern.compile("\\(([^)]+)\\)");

        for (String token : tokens) {
            token = token.trim();
            // Extract parameters, if any.
            Matcher matcher = paramPattern.matcher(token);
            List<String> params = new ArrayList<>();
            while (matcher.find()) {
                params.add(matcher.group(1).trim());
            }
            // Remove all parenthesized parts from the token.
            String baseMovement = token.replaceAll("\\([^)]*\\)", "").trim();

            // Determine which runner this advance applies to.
            Player runner;
            if (baseMovement.startsWith("B")) {
                // Advance starting with B applies to the batter.
                // baseMovement = baseMovement.substring(1).trim();
                runner = state.getBatter();
            } else {
                // Otherwise, assign the next runner in order.
                if (nonBatterIndex < nonBatterRunners.size()) {
                    runner = nonBatterRunners.get(nonBatterIndex++);
                } else {
                    runner = null; // (Log a warning if desired.)
                }
            }

            // Create the RunnerAdvance.
            RunnerAdvance advance = new RunnerAdvance(baseMovement, null, runner);

            // Process parameters: if any parameter is exactly a single digit (1,2,3) or "B", we interpret that as an out.
            List<String> modifiers = new ArrayList<>();
            for (String p : params) {
                if (p.matches("^[123B]$")) {
                    advance.setOut(true);
                } else {
                    modifiers.add(p);
                }
            }
            if (!modifiers.isEmpty()) {
                advance.setDetails(String.join(";", modifiers));
            }

            advances.add(advance);
        }

        // Now update the game state based on each advance.
        // We expect the baseMovement string to be of the form "X-Y" (or "BX2" was already handled above).
        for (RunnerAdvance adv : advances) {
            String move = adv.getBaseMovement();
            if (move.length() >= 3) {
                char fromChar = move.charAt(0);
                char toChar = move.charAt(2);
                int fromBase = Character.isDigit(fromChar) ? Character.getNumericValue(fromChar) : -1;
                // If toChar is H (for home) then we treat that as "0".
                int toBase = (toChar == 'H' || toChar == 'h') ? 0 : (Character.isDigit(toChar) ? Character.getNumericValue(toChar) : -1);

                // Remove runner from his starting base.
                if (fromBase == 1) {
                    state.setFirstBase(null);
                } else if (fromBase == 2) {
                    state.setSecondBase(null);
                } else if (fromBase == 3) {
                    state.setThirdBase(null);
                }

                // If the advance did not result in an out, then put the runner on the destination base.
                if (!adv.isOut()) {
                    if (toBase == 1) {
                        state.setFirstBase(adv.getRunner());
                    } else if (toBase == 2) {
                        state.setSecondBase(adv.getRunner());
                    } else if (toBase == 3) {
                        state.setThirdBase(adv.getRunner());
                    }
                    // For toBase==0 (home), you might want to record a scored run.
                }
            }
        }

        // (Optionally clear the state if you want the next play to start fresh.)
        // For example:
        // state.setBatter(null);
        // state.setFirstBase(null);
        // state.setSecondBase(null);
        // state.setThirdBase(null);

        return advances;
    }

    private void validateRunnerAdvances(String advances) {
        // Split on semicolons to get each advance token.
        String[] parts = advances.split(";");
        for (String part : parts) {
            // Extract the base movement part (everything before the first parenthesis, if present)
            String baseMovement = part.contains("(") ? part.substring(0, part.indexOf("(")) : part;
            baseMovement = baseMovement.trim();
            if (!baseMovement.matches("^[B1-3H](?:[-X][B1-3H])?$")) {
                throw new IllegalArgumentException("Invalid runner advance base movement format: " + baseMovement);
            }
        }
    }

    private String buildDescription() {
        StringBuilder description = new StringBuilder();

        description.append("Event: ");
        if (!playSequence.isEmpty()) {
            for (int i = 0; i < playSequence.size() - 1; i++) {
                description.append(playSequence.get(i).getPositionNumber()).append("-");
            }
            description.append(playSequence.get(playSequence.size() - 1).getPositionNumber()).append(" ");
        }
        for (int i = 0; i < eventTypes.size() - 1; i++) {
            description.append(eventTypes.get(i).getDescription()).append(" ");
        }
        if (!eventTypes.isEmpty()) {
            description.append(eventTypes.get(eventTypes.size() - 1).getDescription());
        }
        if (!runnerAdvances.isEmpty()) {
            description.append(", Runner Advances: ");
            for (RunnerAdvance advance : runnerAdvances) {
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
