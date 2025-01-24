package com.dojonate.statsvisualizer.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team {

    @Id
    @Column(name = "team_id", unique = true, nullable = false)
    private String teamId; // e.g., "HOU"

    @Column(name = "team_name", nullable = false)
    private String name; // e.g., "Houston Astros"

    @Column(name = "league")
    private String league; // e.g., "NL" or "AL"

    @Column(name = "first_year")
    private Integer firstYear; // e.g., 1962 (nullable)

    @Column(name = "last_year")
    private Integer lastYear; // e.g., 2024 (nullable)

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RosterEntry> rosterEntries = new HashSet<>();

    public Team() {}

    public Team(String teamId, String name, String league, Integer firstYear, Integer lastYear) {
        this.teamId = teamId;
        this.name = name;
        this.league = league;
        this.firstYear = firstYear;
        this.lastYear = lastYear;
    }

    // Getters and Setters
    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public Integer getFirstYear() {
        return firstYear;
    }

    public void setFirstYear(Integer firstYear) {
        this.firstYear = firstYear;
    }

    public Integer getLastYear() {
        return lastYear;
    }

    public void setLastYear(Integer lastYear) {
        this.lastYear = lastYear;
    }

    public Set<RosterEntry> getRosterEntries() {
        return rosterEntries;
    }

    public void setRosterEntries(Set<RosterEntry> rosterEntries) {
        this.rosterEntries = rosterEntries;
    }
}
