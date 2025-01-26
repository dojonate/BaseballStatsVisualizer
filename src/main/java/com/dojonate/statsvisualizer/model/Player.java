package com.dojonate.statsvisualizer.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @Column(name = "player_id", unique = true, nullable = false)
    private String playerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "batting_hand")
    private String battingHand;

    @Column(name = "throwing_hand")
    private String throwingHand;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RosterEntry> rosterEntries = new HashSet<>();

    public Player() {
    }

    public Player(String playerId, String firstName, String lastName, String battingHand, String throwingHand, LocalDate birthDate) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.battingHand = battingHand;
        this.throwingHand = throwingHand;
        this.birthDate = birthDate;
    }

    // Getters and Setters

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBattingHand() {
        return battingHand;
    }

    public void setBattingHand(String battingHand) {
        this.battingHand = battingHand;
    }

    public String getThrowingHand() {
        return throwingHand;
    }

    public void setThrowingHand(String throwingHand) {
        this.throwingHand = throwingHand;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Set<RosterEntry> getRosterEntries() {
        return rosterEntries;
    }

    public void setRosterEntries(Set<RosterEntry> rosterEntries) {
        this.rosterEntries = rosterEntries;
    }

    public String getAllTeamNames() {
        return rosterEntries.stream()
                .map(entry -> entry.getTeam().getName()) // Map each roster entry to the team's name
                .distinct() // Ensure no duplicate team names
                .sorted() // Sort alphabetically for consistency
                .collect(Collectors.joining("; ")); // Concatenate with semicolons
    }

}
