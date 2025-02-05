package com.dojonate.statsvisualizer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Calendar;

@Entity
public class Site {
    @Id
    String parkId;

    String name;

    String formerNames;

    String city;

    String state;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    Calendar startDate;

    @DateTimeFormat(pattern = "MM/dd/yyyy")
    Calendar endDate;

    String league;

    String notes;

    // Getters and Setters
    public String getSiteId() {
        return parkId;
    }

    public void setSiteId(String parkId) {
        this.parkId = parkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormerNames() {
        return formerNames;
    }

    public void setFormerNames(String formerNames) {
        this.formerNames = formerNames;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}