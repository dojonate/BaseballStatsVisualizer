package com.dojonate.statsvisualizer.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Game {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    private String site;
    private String date;
    private String number;
    private String starttime;
    private String daynight;
    private boolean usedh;
    private String umphome;
    private String ump1b;
    private String ump2b;
    private String ump3b;
    private String howscored;
    private String pitches;
    private String oscorer;
    private int temp;
    private String winddir;
    private int windspeed;
    private String fieldcond;
    private String precip;
    private String sky;
    private int timeofgame;
    private int attendance;
    private String wp;
    private String lp;
    private String save;

    @OneToMany(mappedBy = "game")
    private List<PlayerEvent> playerEvents;

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(Team awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getDaynight() {
        return daynight;
    }

    public void setDaynight(String daynight) {
        this.daynight = daynight;
    }

    public boolean isUsedh() {
        return usedh;
    }

    public void setUsedh(boolean usedh) {
        this.usedh = usedh;
    }

    public String getUmphome() {
        return umphome;
    }

    public void setUmphome(String umphome) {
        this.umphome = umphome;
    }

    public String getUmp1b() {
        return ump1b;
    }

    public void setUmp1b(String ump1b) {
        this.ump1b = ump1b;
    }

    public String getUmp2b() {
        return ump2b;
    }

    public void setUmp2b(String ump2b) {
        this.ump2b = ump2b;
    }

    public String getUmp3b() {
        return ump3b;
    }

    public void setUmp3b(String ump3b) {
        this.ump3b = ump3b;
    }

    public String getHowscored() {
        return howscored;
    }

    public void setHowscored(String howscored) {
        this.howscored = howscored;
    }

    public String getPitches() {
        return pitches;
    }

    public void setPitches(String pitches) {
        this.pitches = pitches;
    }

    public String getOscorer() {
        return oscorer;
    }

    public void setOscorer(String oscorer) {
        this.oscorer = oscorer;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getWinddir() {
        return winddir;
    }

    public void setWinddir(String winddir) {
        this.winddir = winddir;
    }

    public int getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(int windspeed) {
        this.windspeed = windspeed;
    }

    public String getFieldcond() {
        return fieldcond;
    }

    public void setFieldcond(String fieldcond) {
        this.fieldcond = fieldcond;
    }

    public String getPrecip() {
        return precip;
    }

    public void setPrecip(String precip) {
        this.precip = precip;
    }

    public String getSky() {
        return sky;
    }

    public void setSky(String sky) {
        this.sky = sky;
    }

    public int getTimeofgame() {
        return timeofgame;
    }

    public void setTimeofgame(int timeofgame) {
        this.timeofgame = timeofgame;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public String getWp() {
        return wp;
    }

    public void setWp(String wp) {
        this.wp = wp;
    }

    public String getLp() {
        return lp;
    }

    public void setLp(String lp) {
        this.lp = lp;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public List<PlayerEvent> getPlayerEvents() {
        return playerEvents;
    }

    public void setPlayerEvents(List<PlayerEvent> playerEvents) {
        this.playerEvents = playerEvents;
    }
}