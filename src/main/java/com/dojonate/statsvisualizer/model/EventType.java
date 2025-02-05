package com.dojonate.statsvisualizer.model;

public enum EventType {
    // Batter Events
    SINGLE("S", "Single"),
    DOUBLE("D", "Double"),
    TRIPLE("T", "Triple"),
    HOME_RUN("H", "Home Run"),
    WALK("W", "Walk"),
    INTENTIONAL_WALK("IW", "Intentional Walk"),
    HIT_BY_PITCH("HP", "Hit by Pitch"),
    STRIKEOUT("K", "Strikeout"),
    NO_PLAY("NP", "No Play"),
    ERROR("E", "Error"),
    ERROR_ON_FOUL_FLY_BALL("FLE", "Error on Foul Fly Ball"),
    GROUND_RULE_DOUBLE("DGR", "Ground Rule Double"),
    CATCHER_INTERFERENCE("C/E", "Catcher Interference"),
    FIELDERS_CHOICE("FC", "Fielder's Choice"),
    FORCE_OUT("FO", "Force Out"),
    SACRIFICE_FLY("SF", "Sacrifice Fly"),
    SACRIFICE_HIT_BUNT("SH", "Sacrifice Hit (Bunt)"),
    BATTED_BALL_INTERFERENCE("BR", "Batted Ball Interference"),
    INSIDE_THE_PARK_HOME_RUN("IPHR", "Inside-the-Park Home Run"),
    LINED_INTO_DOUBLE_PLAY("LDP", "Lined Into Double Play"),
    LINED_INTO_TRIPLE_PLAY("LTP", "Lined Into Triple Play"),
    OBSTRUCTION("OBS", "Obstruction"),
    BATTERS_INTERFERENCE("BINT", "Batter's Interference"),

    // Runner Events
    BALK("BK", "Balk"),
    CAUGHT_STEALING("CS", "Caught Stealing"),
    DEFENSIVE_INDIFFERENCE("DI", "Defensive Indifference"),
    OUT_ADVANCING("OA", "Out Advancing"),
    PASSED_BALL("PB", "Passed Ball"),
    WILD_PITCH("WP", "Wild Pitch"),
    PICK_OFF("PO", "Pick Off"),
    PICK_OFF_CAUGHT_STEALING("POCS", "Pick Off Caught Stealing"),
    STOLEN_BASE("SB", "Stolen Base"),

    // Modifiers
    APPEAL_PLAY("AP", "Appeal Play"),
    BUNT_GROUND_BALL("BG", "Ground Ball Bunt"),
    BUNT_GROUNDED_INTO_DOUBLE_PLAY("BGDP", "Bunt Grounded Into Double Play"),
    BATTER_INTERFERENCE("BINT", "Batter Interference"),
    BUNT_LINE_DRIVE("BL", "Line Drive Bunt"),
    BATTING_OUT_OF_TURN("BOOT", "Batting Out of Turn"),
    BUNT_POP_UP("BP", "Bunt Pop Up"),
    BUNT_POPPED_INTO_DOUBLE_PLAY("BPDP", "Bunt Popped Into Double Play"),
    RUNNER_HIT_BY_BATTED_BALL("BR", "Runner Hit by Batted Ball"),
    CALLED_THIRD_STRIKE("C", "Called Third Strike"),
    COURTESY_BATTER("COUB", "Courtesy Batter"),
    COURTESY_FIELDER("COUF", "Courtesy Fielder"),
    COURTESY_RUNNER("COUR", "Courtesy Runner"),
    UNSPECIFIED_DOUBLE_PLAY("DP", "Unspecified Double Play"),
    ERROR_ON_FIELDER("$E", "Error on Fielder"),
    FLY("F", "Fly"),
    FLY_BALL_DOUBLE_PLAY("FDP", "Fly Ball Double Play"),
    FAN_INTERFERENCE("FINT", "Fan Interference"),
    FOUL("FL", "Foul"),
    GROUND_BALL("G", "Grounder"),
    GROUND_BALL_DOUBLE_PLAY("GDP", "Ground Ball Double Play"),
    GROUND_BALL_TRIPLE_PLAY("GTP", "Ground Ball Triple Play"),
    INFIELD_FLY_RULE("IF", "Infield Fly Rule"),
    INTERFERENCE("INT", "Interference"),
    LINE_DRIVE("L", "Line Drive"),
    MANAGER_CHALLENGE("MREV", "Manager Challenge"),
    NO_DOUBLE_PLAY("NDP", "No Double Play"),
    POP_FLY("P", "Pop Fly"),
    RUNNER_PASSED("PASS", "Runner Passed"),
    RELAY_THROW("R$", "Relay Throw"),
    RUNNER_INTERFERENCE("RINT", "Runner Interference"),
    SACRIFICE_HIT("SH", "Sacrifice Hit"),
    THROW("TH", "Throw"),
    THROW_TO_BASE("TH", "Throw to Base"),
    UNSPECIFIED_TRIPLE_PLAY("TP", "Unspecified Triple Play"),
    UMPIRE_INTERFERENCE("UINT", "Umpire Interference"),
    UMPIRE_REVIEW("UREV", "Umpire Review");

    private final String abbreviation;
    private final String description;

    EventType(String abbreviation, String description) {
        this.abbreviation = abbreviation;
        this.description = description;
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

    public String getDescription() {
        return description;
    }
}
