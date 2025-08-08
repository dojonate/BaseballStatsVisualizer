package com.dojonate.statsvisualizer.model;

/**
 * Immutable representation of a single play from a Retrosheet event file.
 */
public record Play(
        int inning,
        boolean homeTeam,
        String playerId,
        String count,
        String pitches,
        String event
) {
    // Retain a conventional Java bean-style accessor for boolean property
    public boolean isHomeTeam() {
        return homeTeam;
    }
}
