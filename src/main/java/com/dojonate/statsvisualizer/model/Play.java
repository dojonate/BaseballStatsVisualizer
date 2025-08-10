package com.dojonate.statsvisualizer.model;

import java.util.Map;

/**
 * Immutable representation of a single play from a Retrosheet event file.
 */
public record Play(
        int inning,
        boolean homeTeam,
        String playerId,
        String count,
        String pitches,
        String event,
        Map<Character, String> runnerAdvances
) {
    public Play {
        runnerAdvances = runnerAdvances == null ? Map.of() : Map.copyOf(runnerAdvances);
    }

    // Retain a conventional Java bean-style accessor for boolean property
    public boolean isHomeTeam() {
        return homeTeam;
    }
}
