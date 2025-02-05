package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.EventType;
import com.dojonate.statsvisualizer.model.Position;
import com.dojonate.statsvisualizer.model.RunnerAdvance;

import java.util.ArrayList;
import java.util.List;

public class EventDetails {
    private final List<EventType> eventType;
    private String description;
    private List<RunnerAdvance> runnerAdvances; // e.g., "1-2", "2-H"
    private final List<Position> playSequence;

    public EventDetails() {
        this.eventType = new ArrayList<>();
        this.runnerAdvances = new ArrayList<>();
        this.playSequence = new ArrayList<>();
    }

    public EventDetails(String event) {
        this();
        parseEventDetails(event);
    }

    // Getters and Setters
    public List<EventType> getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType.add(eventType);
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

    private void parseEventDetails(String event) {
        String[] eventParts = event.split("\\.", 2); // Split at the first period for modifiers and runner advances

        // Extract event type and modifiers
        String[] primaryParts = eventParts[0].split("/");
        for (String part : primaryParts) {
            if (part.contains("+")) {
                // Handle special cases (e.g., "K+")
                String[] subEvent = part.split("\\+");
                for (String subPart : subEvent) {
                    eventType.add(EventType.fromAbbreviation(subPart));
                }
            } else if (part.contains("-")) {
                part = part.replace("-", "");
                eventType.add(EventType.fromAbbreviation(part));
            } else if (part.matches(".*\\d.*")) { // Check if part contains a digit
                String eventPart = part.replaceAll("[^a-zA-Z]", ""); // Extract alphabet characters
                String positionsPart = part.replaceAll("[^\\d()]", ""); // Extract digit characters and parentheses
                if (!positionsPart.isEmpty() && eventPart.isEmpty()) {
                    boolean isRunner = false;
                    // Handle multiple player references (e.g., "23")
                    for (char position : positionsPart.toCharArray()) {
                        if (position == '(' || position == ')') {
                            isRunner = position == '(';
                            continue;
                        } else if (isRunner) {
                            isRunner = false;
                            // TODO: Store runner out
                            continue;
                        }
                        int index = Character.getNumericValue(position);
                        Position.fromPositionNumber(index); // Placeholder handling
                        playSequence.add(Position.fromPositionNumber(Character.getNumericValue(position)));
                    }
                } else {
                    System.out.println("Player reference ID(s): " + positionsPart); // Placeholder handling
                    if (eventPart.equals("HR")) {
                        eventType.add(EventType.INSIDE_THE_PARK_HOME_RUN);
                    } else {
                        eventType.add(EventType.fromAbbreviation(eventPart));
                    }
                }
            } else {
                setEventType(EventType.fromAbbreviation(part)); // First part is the event type (e.g., "S8", "K", "HR")
            }
        }

        // Parse runner advances (e.g., "2-H", "1-3", "BX2")
        if (eventParts.length > 1) {
            List<RunnerAdvance> runnerAdvances = parseRunnerAdvances(eventParts[1]);
            setRunnerAdvances(runnerAdvances);
        }

        // Build a readable description
        setDescription(buildDescription());
    }

    private List<RunnerAdvance> parseRunnerAdvances(String runnerData) {
        validateRunnerAdvances(runnerData);
        List<RunnerAdvance> advances = new ArrayList<>();
        String[] advancesParts = runnerData.split(";"); // Split runner advances by semicolon

        for (String advance : advancesParts) {
            // Separate base movement and additional data (e.g., "2-H(E4)")
            String[] advanceParts = advance.split("\\(", 2);
            String baseMovement = advanceParts[0];
            String details = advanceParts.length > 1 ? advanceParts[1].replace(")", "") : null;

            advances.add(new RunnerAdvance(baseMovement, details));
        }

        return advances;
    }

    private void validateRunnerAdvances(String advances) {
        String[] parts = advances.split(";");
        for (String part : parts) {
            if (!part.matches("[B1-3]-[1-3H](\\(.*\\))?")) {
                throw new IllegalArgumentException("Invalid runner advance format.");
            }
        }
    }

    private String buildDescription() {
        StringBuilder description = new StringBuilder();
        description.append("Event: ");
        if (!playSequence.isEmpty()) {
            for (Position position : playSequence.subList(0, playSequence.size() - 1)) {
                description.append(position.getPositionNumber()).append("-");
            }
            description.append(playSequence.get(playSequence.size() - 1).getPositionNumber()).append(" ");
        }
        for (EventType event : getEventType().subList(0, getEventType().size() - 1)) {
            description.append(event.getDescription()).append(" ");
        }
        description.append(getEventType().get(getEventType().size() - 1).getDescription());
        if (!getRunnerAdvances().isEmpty()) {
            description.append(", Runner Advances: ");
            for (RunnerAdvance advance : getRunnerAdvances()) {
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