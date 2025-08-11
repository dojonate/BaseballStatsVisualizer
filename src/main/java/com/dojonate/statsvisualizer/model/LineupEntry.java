package com.dojonate.statsvisualizer.model;

/**
 * Represents a starting lineup slot linking a player to a batting order and fielding position.
 */
public record LineupEntry(Player player, int battingOrder, int fieldingPosition) {
}
