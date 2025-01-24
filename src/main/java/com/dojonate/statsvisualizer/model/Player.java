package com.dojonate.statsvisualizer.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_id", unique = true, nullable = false)
    private String playerId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "team", nullable = false)
    private String team;

    @Column(name = "position", nullable = false)
    private String position;

    @Column(name = "batting_hand")
    private String battingHand;

    @Column(name = "throwing_hand")
    private String throwingHand;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public Player() {
    }

    public Player(String playerId, String firstName, String lastName, String team, String position,
                  String battingHand, String throwingHand, LocalDate birthDate) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.team = team;
        this.position = position;
        this.battingHand = battingHand;
        this.throwingHand = throwingHand;
        this.birthDate = birthDate;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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
}
