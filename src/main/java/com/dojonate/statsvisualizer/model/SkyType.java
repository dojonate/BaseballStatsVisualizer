package com.dojonate.statsvisualizer.model;

public enum SkyType {
    CLOUDY("cloudy"),
    DOME("dome"),
    NIGHT("night"),
    OVERCAST("overcast"),
    SUNNY("sunny"),
    UNKNOWN("unknown");

    private final String description;

    SkyType(String description) {
        this.description = description;
    }

    public static SkyType fromString(String input) {
        for (SkyType type : SkyType.values()) {
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
