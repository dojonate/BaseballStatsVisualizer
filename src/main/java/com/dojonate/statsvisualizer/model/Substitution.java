package com.dojonate.statsvisualizer.model;

/**
 * Represents a lineup change or substitution occurring during a game.
 */
public record Substitution(
        Player playerIn,
        Player playerOut,
        int battingOrder,
        int fieldingPosition,
        int inning,
        boolean homeHalf
) {}
