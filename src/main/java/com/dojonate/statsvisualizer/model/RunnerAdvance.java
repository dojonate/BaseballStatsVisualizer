package com.dojonate.statsvisualizer.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class RunnerAdvance {
    private String baseMovement; // e.g., "2-H", "1-3"
    private String details;      // e.g., "E4", "GDP"

    // No-arg constructor is required
    public RunnerAdvance() {
    }

    public RunnerAdvance(String baseMovement, String details) {
        this.baseMovement = baseMovement;
        this.details = details;
    }

    public String getBaseMovement() {
        return baseMovement;
    }

    public void setBaseMovement(String baseMovement) {
        this.baseMovement = baseMovement;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
