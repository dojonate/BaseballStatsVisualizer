package com.dojonate.statsvisualizer.model;

import java.util.*;

/**
 * Represents a single game parsed from a Retrosheet event file.
 */
public class RetrosheetGame {
    private String gameId;
    private final Map<String, String> info = new LinkedHashMap<>();
    private final List<Play> plays = new ArrayList<>();
    private final List<LineupEntry> visitorLineup = new ArrayList<>();
    private final List<LineupEntry> homeLineup = new ArrayList<>();

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Map<String, String> getInfo() {
        return Collections.unmodifiableMap(info);
    }

    public List<Play> getPlays() {
        return Collections.unmodifiableList(plays);
    }

    public List<LineupEntry> getVisitorLineup() {
        return Collections.unmodifiableList(visitorLineup);
    }

    public List<LineupEntry> getHomeLineup() {
        return Collections.unmodifiableList(homeLineup);
    }

    public void addInfo(String key, String value) {
        info.put(key, value);
    }

    public void addPlay(Play play) {
        plays.add(play);
    }

    public void addLineupEntry(boolean home, LineupEntry entry) {
        (home ? homeLineup : visitorLineup).add(entry);
    }
}
