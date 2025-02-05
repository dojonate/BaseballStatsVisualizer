package com.dojonate.statsvisualizer.model;

public enum WindDirection {
    FROM_CENTER_FIELD("fromcf"),
    FROM_LEFT_FIELD("fromlf"),
    FROM_RIGHT_FIELD("fromrf"),
    LEFT_TO_RIGHT("ltor"),
    RIGHT_TO_LEFT("rtol"),
    TO_CENTER_FIELD("tocf"),
    TO_LEFT_FIELD("tolf"),
    TO_RIGHT_FIELD("torf"),
    UNKNOWN("unknown");

    private final String description;

    WindDirection(String description) {
        this.description = description;
    }

    public static WindDirection fromString(String input) {
        for (WindDirection direction : WindDirection.values()) {
            if (direction.description.equalsIgnoreCase(input)) {
                return direction;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return description;
    }
}
