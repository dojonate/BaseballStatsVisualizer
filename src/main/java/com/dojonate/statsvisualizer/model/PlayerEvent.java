package com.dojonate.statsvisualizer.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class PlayerEvent {

    private final List<EventType> eventType;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player batter;
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    private int balls;

    private int strikes;

    private int inning;

    private String description;

    @ManyToOne
    @JoinColumn(name = "batting_team_id")
    private Team battingTeam;

    @ManyToOne
    @JoinColumn(name = "fielding_team_id")
    private Team fieldingTeam;

    @ElementCollection
    @CollectionTable(name = "player_event_runner_advances", joinColumns = @JoinColumn(name = "event_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "baseMovement", column = @Column(name = "base_movement")),
            @AttributeOverride(name = "details", column = @Column(name = "details"))
    })
    private List<RunnerAdvance> runnerAdvances; // Store runner advances like "1-3"

    private List<PitchType> pitchDetails;

    public PlayerEvent() {
        this.eventType = new ArrayList<>();
        this.runnerAdvances = new ArrayList<>();
        this.pitchDetails = new ArrayList<>();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getBatter() {
        return batter;
    }

    public void setBatter(Player batter) {
        this.batter = batter;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<EventType> getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType.add(eventType);
    }

    public void setEventType(List<EventType> eventType) {
        this.eventType.addAll(eventType);
    }

    public int getInning() {
        return inning;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Team getBattingTeam() {
        return battingTeam;
    }

    public void setBattingTeam(Team battingTeam) {
        this.battingTeam = battingTeam;
    }

    public Team getFieldingTeam() {
        return fieldingTeam;
    }

    public void setFieldingTeam(Team fieldingTeam) {
        this.fieldingTeam = fieldingTeam;
    }

    public List<RunnerAdvance> getRunnerAdvances() {
        return runnerAdvances;
    }

    public void setRunnerAdvances(List<RunnerAdvance> runnerAdvances) {
        this.runnerAdvances = runnerAdvances;
    }

    public void setCount(int balls, int strikes) {
        this.balls = balls;
        this.strikes = strikes;
    }

    public int[] getCount() {
        return new int[]{balls, strikes};
    }

    public int getBalls() {
        return balls;
    }

    public int getStrikes() {
        return strikes;
    }

    public List<PitchType> getPitchList() {
        return pitchDetails;
    }

    public void setPitchList(List<PitchType> pitchDetails) {
        this.pitchDetails = pitchDetails;
    }
}
