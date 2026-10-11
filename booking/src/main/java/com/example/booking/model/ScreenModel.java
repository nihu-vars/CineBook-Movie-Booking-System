package com.example.booking.model;

import jakarta.persistence.*;

@Entity
public class ScreenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long screenId;
    private int totalSeats;
    private int seatsPerRow;
    private int regularRows;
    private int premiumRows;
    private int reclinerRows;
    private String screenName;
    @ManyToOne
    @JoinColumn(name = "theatreId")   //this tells hibernate to create a column with theatreId // instead of this we could have simply put private Long theatreId;
    private TheatreModel theatre;

    public TheatreModel getTheatre() {
        return theatre;
    }

    public void setTheatre(TheatreModel theatre) {
        this.theatre = theatre;
    }

    public ScreenModel(){}

    public Long getScreenId() {
        return screenId;
    }
    public int getSeatsPerRow() {
        return seatsPerRow;
    }

    public int getRegularRows() {
        return regularRows;
    }

    public void setRegularRows(int regularRows) {
        this.regularRows = regularRows;
    }
    public void setSeatsPerRow(int seatsPerRow) {
        this.seatsPerRow = seatsPerRow;
    }
    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getReclinerRows() {
        return reclinerRows;
    }

    public void setReclinerRows(int reclinerRows) {
        this.reclinerRows = reclinerRows;
    }

    public int getPremiumRows() {
        return premiumRows;
    }

    public void setPremiumRows(int premiumRows) {
        this.premiumRows = premiumRows;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }



}
