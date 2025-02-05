package com.dojonate.statsvisualizer.model;

public enum GameType {
    REGULAR("regular"),
    EXHIBITION("exhibition"),
    PRESEASON("preseason"),
    ALLSTAR("allstar"),
    PLAYOFF("playoff"),
    WORLDSERIES("worldseries"),
    LCS("lcs"),
    DIVISIONSERIES("divisionseries"),
    WILDCARD("wildcard"),
    CHAMPIONSHIP("championship");

    private final String description;

    GameType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
