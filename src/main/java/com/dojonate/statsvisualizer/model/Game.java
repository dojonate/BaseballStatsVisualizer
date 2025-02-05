package com.dojonate.statsvisualizer.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.List;

@Entity
public class Game {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "home_team_id")
    private Team homeTeam;

    @OneToOne
    @JoinColumn(name = "away_team_id")
    private Team awayTeam;

    @OneToOne
    @JoinColumn(name = "site_id")
    private Site site;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mma")
    private final Calendar date;

    private Integer gameNumber;
    private String startTime;
    private boolean nightGame;
    private boolean useDesignatedHitter;
    private String umphome;
    private String ump1b;
    private String ump2b;
    private String ump3b;
    private String pitches;
    private String officialScorer;
    private int temperature;
    private WindDirection windDirection;
    private int windSpeed;
    private FieldConditions fieldConditions;
    private PrecipitationType precipitation;
    private SkyType sky;
    private int lengthOfGame;
    private int attendance;

    @ManyToOne
    @JoinColumn(name = "winning_pitcher_id", referencedColumnName = "player_id")
    private Player wp;

    @ManyToOne
    @JoinColumn(name = "losing_pitcher_id", referencedColumnName = "player_id")
    private Player lp;

    @ManyToOne
    @JoinColumn(name = "save_pitcher_id", referencedColumnName = "player_id")
    private Player save;

    private GameType gameType;

    @OneToMany(mappedBy = "game")
    private List<PlayerEvent> playerEvents;

    public Game() {
        this.date = Calendar.getInstance();
    }

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

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(TemporalAccessor date) {
        this.date.set(date.get(ChronoField.YEAR), date.get(ChronoField.MONTH_OF_YEAR), date.get(ChronoField.DAY_OF_MONTH), date.get(ChronoField.HOUR_OF_DAY), date.get(ChronoField.MINUTE_OF_HOUR));
    }

    public Integer getGameNumber() {
        return gameNumber;
    }

    public void setGameNumber(Integer gameNumber) {
        this.gameNumber = gameNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public boolean getNightGame() {
        return nightGame;
    }

    public void setNightGame(boolean nightGame) {
        this.nightGame = nightGame;
    }

    public boolean isUseDesignatedHitter() {
        return useDesignatedHitter;
    }

    public void setUseDesignatedHitter(boolean useDesignatedHitter) {
        this.useDesignatedHitter = useDesignatedHitter;
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

    public String getPitches() {
        return pitches;
    }

    public void setPitches(String pitches) {
        this.pitches = pitches;
    }

    public String getOfficialScorer() {
        return officialScorer;
    }

    public void setOfficialScorer(String officialScorer) {
        this.officialScorer = officialScorer;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public WindDirection getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(WindDirection windDirection) {
        this.windDirection = windDirection;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public FieldConditions getFieldConditions() {
        return fieldConditions;
    }

    public void setFieldConditions(FieldConditions fieldConditions) {
        this.fieldConditions = fieldConditions;
    }

    public PrecipitationType getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(PrecipitationType precipitation) {
        this.precipitation = precipitation;
    }

    public SkyType getSky() {
        return sky;
    }

    public void setSky(SkyType sky) {
        this.sky = sky;
    }

    public int getLengthOfGame() {
        return lengthOfGame;
    }

    public void setLengthOfGame(int lengthOfGame) {
        this.lengthOfGame = lengthOfGame;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public Player getWp() {
        return wp;
    }

    public void setWp(Player wp) {
        this.wp = wp;
    }

    public Player getLp() {
        return lp;
    }

    public void setLp(Player lp) {
        this.lp = lp;
    }

    public Player getSave() {
        return save;
    }

    public void setSave(Player save) {
        this.save = save;
    }

    public List<PlayerEvent> getPlayerEvents() {
        return playerEvents;
    }

    public void setPlayerEvents(List<PlayerEvent> playerEvents) {
        this.playerEvents = playerEvents;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }
}