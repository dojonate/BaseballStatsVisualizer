package com.dojonate.statsvisualizer.model;

public enum PitchType {
    BLOCKED_PITCH('*'),
    PICKOFF_TO_FIRST('1'),
    PICKOFF_TO_SECOND('2'),
    PICKOFF_TO_THIRD('3'),
    RUNNER_GOING('>'),
    AUTOMATIC_STRIKE('A'),
    BALL('B'),
    CALLED_STRIKE('C'),
    FOUL('F'),
    HIT_BATTER('H'),
    INTENTIONAL_BALL('I'),
    FOUL_BUNT('L'),
    MISSED_BUNT_ATTEMPT('M'),
    NO_PITCH('N'), // for balks and interference calls
    FOUL_TIP_BUNT('O'),
    PITCHOUT('P'),
    SWINGING_ON_PITCHOUT('Q'),
    FOUL_ON_PITCHOUT('R'),
    SWINGING_STRIKE('S'),
    FOUL_TIP('T'),
    UNKNOWN('U'),
    CALLED_BALL_ANOMALY('V'),
    BALL_IN_PLAY_BATTER('X'),
    BALL_IN_PLAY_PITCHOUT('Y');

    private final Character abbreviation;

    PitchType(Character abbreviation) {
        this.abbreviation = abbreviation;
    }

    public static PitchType fromAbbreviation(Character abbreviation) {
        for (PitchType pitchType : PitchType.values()) {
            if (pitchType.getAbbreviation().equals(abbreviation)) {
                return pitchType;
            }
        }
        return null;
    }

    public Character getAbbreviation() {
        return abbreviation;
    }
}
