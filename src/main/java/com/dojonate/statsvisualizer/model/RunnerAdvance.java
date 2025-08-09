package com.dojonate.statsvisualizer.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Embeddable
public class RunnerAdvance {
    @ManyToOne
    @JoinColumn(name = "player_id")
    private transient Player runner;

    private String baseMovement; // e.g., "2-H", "1-3"
    private String details;      // e.g., "E4", "GDP"
    private boolean out;

    // No-arg constructor is required
    public RunnerAdvance() {
    }

    public RunnerAdvance(String baseMovement, String details, Player runner) {
        this.baseMovement = baseMovement;
        this.details = details;
        this.runner = runner;
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

    public Player getRunner() {
        return runner;
    }

    public void setRunner(Player runner) {
        this.runner = runner;
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    public boolean isOut() {
        return out;
    }
}
