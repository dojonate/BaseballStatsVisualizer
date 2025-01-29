package com.dojonate.statsvisualizer.util;

import com.dojonate.statsvisualizer.model.EventType;
import com.dojonate.statsvisualizer.model.RunnerAdvance;

import java.util.ArrayList;
import java.util.List;

public class EventDetails {
    private final List<EventType> eventType;
    private String description;
    private List<RunnerAdvance> runnerAdvances; // e.g., "1-2", "2-H"

    public EventDetails() {
        this.eventType = new ArrayList<>();
        this.runnerAdvances = new ArrayList<>();
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
                String eventPart = part.replaceAll("\\d", ""); // Extract alphabet characters
                String positionsPart = part.replaceAll("\\D", ""); // Extract digit characters
                // TODO: handle digits as player references
                System.out.println("Player reference ID(s): " + positionsPart); // Placeholder handling
                if (eventPart.equals("HR")) {
                    eventType.add(EventType.INSIDE_THE_PARK_HOME_RUN);
                } else {
                    eventType.add(EventType.fromAbbreviation(eventPart));
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
        description.append("Event: ").append(getEventType());
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