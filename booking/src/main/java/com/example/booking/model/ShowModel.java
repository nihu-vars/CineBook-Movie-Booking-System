package com.example.booking.model;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalTime;
//Movie
//  |
//          | 1
//          |
//          | *
//Show
//  |
//          | *
//          |
//          | 1
//Screen
@Entity
public class ShowModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showId;
    @ManyToOne
    @JoinColumn(name = "screenId")
    private ScreenModel screenModel;

    private LocalDate showDate;
    private double price;
    private LocalTime showStartTime;
    private LocalTime showEndTime;
    @ManyToOne
    @JoinColumn(name="movieId")
    private MovieModel movieModel;

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public MovieModel getMovieModel() {
        return movieModel;
    }

    public void setMovieModel(MovieModel movieModel) {
        this.movieModel = movieModel;
    }

    public ScreenModel getScreenModel() {
        return screenModel;
    }

    public void setScreenModel(ScreenModel screenModel) {
        this.screenModel = screenModel;
    }

    public LocalDate getShowDate() {
        return showDate;
    }

    public void setShowDate(LocalDate showDate) {
        this.showDate = showDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalTime getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(LocalTime showStartTime) {
        this.showStartTime = showStartTime;
    }

    public LocalTime getShowEndTime() {
        return showEndTime;
    }

    public void setShowEndTime(LocalTime showEndTime) {
        this.showEndTime = showEndTime;
    }

}
