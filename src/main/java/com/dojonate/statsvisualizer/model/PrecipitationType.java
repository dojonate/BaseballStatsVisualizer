package com.dojonate.statsvisualizer.model;

public enum PrecipitationType {
    NONE("none"),
    DRIZZLE("drizzle"),
    RAIN("rain"),
    SHOWERS("showers"),
    SNOW("snow"),
    UNKNOWN("unknown");

    private final String description;

    PrecipitationType(String description) {
        this.description = description;
    }

    public static PrecipitationType fromString(String input) {
        for (PrecipitationType type : PrecipitationType.values()) {
            if (type.description.equalsIgnoreCase(input)) {
                return type;
            }
        }
        return UNKNOWN;
    }

    @Override
    public String toString() {
        return description;
    }
}
