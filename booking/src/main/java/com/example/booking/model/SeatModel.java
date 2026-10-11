package com.example.booking.model;

import jakarta.persistence.*;
//seat belongs to screen not to show
@Entity
public class SeatModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seatId;
    private String seatNumber;
    private String seatType;
    @ManyToOne
    @JoinColumn(name = "screenId")
    private ScreenModel screenModel;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public ScreenModel getScreenModel() {
        return screenModel;
    }

    public void setScreenModel(ScreenModel screenModel) {
        this.screenModel = screenModel;
    }

}
