package com.dojonate.statsvisualizer.model;

public enum EventType {
    // Batter Events
    SINGLE("S"),
    DOUBLE("D"),
    TRIPLE("T"),
    HOME_RUN("H"),
    WALK("W"),
    INTENTIONAL_WALK("IW"),
    HIT_BY_PITCH("HP"),
    STRIKEOUT("K"),
    NO_PLAY("NP"),
    ERROR("E"),
    ERROR_ON_FOUL_FLY_BALL("FLE"),
    GROUND_RULE_DOUBLE("DGR"),
    CATCHER_INTERFERENCE("C/E"),
    FIELDERS_CHOICE("FC"),
    FORCE_OUT("FO"),
    SACRIFICE_FLY("SF"),
    SACRIFICE_HIT_BUNT("SH"),
    BATTED_BALL_INTERFERENCE("BR"),
    INSIDE_THE_PARK_HOME_RUN("IPHR"),
    DOUBLE_PLAY("GDP"),
    TRIPLE_PLAY("GTP"),
    LINED_INTO_DOUBLE_PLAY("LDP"),
    LINED_INTO_TRIPLE_PLAY("LTP"),
    OBSTRUCTION("OBS"),
    BATTERS_INTERFERENCE("BINT"),

    // Runner Events
    BALK("BK"),
    CAUGHT_STEALING("CS"),
    DEFENSIVE_INDIFFERENCE("DI"),
    OUT_ADVANCING("OA"),
    PASSED_BALL("PB"),
    WILD_PITCH("WP"),
    PICK_OFF("PO"),
    PICK_OFF_CAUGHT_STEALING("POCS"),
    STOLEN_BASE("SB"),

    // Modifiers
    APPEAL_PLAY("AP"),
    POP_UP_BUNT("BP"),
    GROUND_BALL_BUNT("BG"),
    BUNT_GROUNDED_INTO_DOUBLE_PLAY("BGDP"),
    BATTER_INTERFERENCE("BINT"),
    LINE_DRIVE_BUNT("BL"),
    BATTING_OUT_OF_TURN("BOOT"),
    BUNT_POP_UP("BP"),
    BUNT_POPPED_INTO_DOUBLE_PLAY("BPDP"),
    RUNNER_HIT_BY_BATTED_BALL("BR"),
    CALLED_THIRD_STRIKE("C"),
    COURTESY_BATTER("COUB"),
    COURTESY_FIELDER("COUF"),
    COURTESY_RUNNER("COUR"),
    UNSPECIFIED_DOUBLE_PLAY("DP"),
    ERROR_ON_FIELDER("$E"),
    FLY("F"),
    FLY_BALL_DOUBLE_PLAY("FDP"),
    FAN_INTERFERENCE("FINT"),
    FOUL("FL"),
    GROUND_BALL("G"),
    GROUND_BALL_DOUBLE_PLAY("GDP"),
    GROUND_BALL_TRIPLE_PLAY("GTP"),
    INFIELD_FLY_RULE("IF"),
    INTERFERENCE("INT"),
    LINE_DRIVE("L"),
    MANAGER_CHALLENGE("MREV"),
    NO_DOUBLE_PLAY("NDP"),
    POP_FLY("P"),
    RUNNER_PASSED("PASS"),
    RELAY_THROW("R$"),
    RUNNER_INTERFERENCE("RINT"),
    SACRIFICE_HIT("SH"),
    THROW("TH"),
    THROW_TO_BASE("TH"),
    UNSPECIFIED_TRIPLE_PLAY("TP"),
    UMPIRE_INTERFERENCE("UINT"),
    UMPIRE_REVIEW("UREV");

    private final String abbreviation;

    EventType(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static EventType fromAbbreviation(String abbreviation) {
        for (EventType eventType : EventType.values()) {
            if (eventType.getAbbreviation().equals(abbreviation)) {
                return eventType;
            }
        }
        return null;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
