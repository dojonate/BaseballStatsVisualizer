package com.dojonate.statsvisualizer.model;

public enum Position {
    PITCHER(1, "Pitcher", "P"),
    CATCHER(2, "Catcher", "C"),
    FIRST_BASE(3, "First Base", "1B"),
    SECOND_BASE(4, "Second Base", "2B"),
    THIRD_BASE(5, "Third Base", "3B"),
    SHORTSTOP(6, "Shortstop", "SS"),
    LEFT_FIELD(7, "Left Field", "LF"),
    CENTER_FIELD(8, "Center Field", "CF"),
    RIGHT_FIELD(9, "Right Field", "RF"),
    DESIGNATED_HITTER(10, "Designated Hitter", "DH"),
    PINCH_HITTER(11, "Pinch Hitter", "PH"),
    PINCH_RUNNER(12, "Pinch Runner", "PR");

    private final int positionNumber; // e.g., 1 for Pitcher, 2 for Catcher
    private final String name;        // e.g., Pitcher, Catcher
    private final String abbreviation; // e.g., P, C

    Position(int positionNumber, String name, String abbreviation) {
        this.positionNumber = positionNumber;
        this.name = name;
        this.abbreviation = abbreviation;
    }

    public int getPositionNumber() {
        return positionNumber;
    }

    public String getName() {
        return name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
