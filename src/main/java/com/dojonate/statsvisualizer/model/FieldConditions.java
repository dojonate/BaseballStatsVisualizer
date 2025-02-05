package com.dojonate.statsvisualizer.model;

public enum FieldConditions {
    DRY("dry"),
    SOAKED("soaked"),
    WET("wet"),
    UNKNOWN("unknown");

    private final String description;

    FieldConditions(String description) {
        this.description = description;
    }

    public static FieldConditions fromDescription(String description) {
        for (FieldConditions condition : FieldConditions.values()) {
            if (condition.description.equalsIgnoreCase(description)) {
                return condition;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return description;
    }
}
