package com.example.booking.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class BookingModel {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long bookingId;
    private double bookingAmount;
    private String bookingStatus;
    private LocalDate bookingDate;
    @ManyToOne
    @JoinColumn(name = "showId")
    private ShowModel showModel;
    @ManyToOne
    @JoinColumn(name = "userId")
    private UserModel userModel;
    @Column(name = "hold_expires_at")
    private LocalDateTime holdExpiresAt;

    public UserModel getUserModel() {
        return userModel;
    }

    public BookingModel() {}

    public LocalDateTime getHoldExpiresAt() {
        return holdExpiresAt;
    }

    public void setHoldExpiresAt(LocalDateTime holdExpiresAt) {
        this.holdExpiresAt = holdExpiresAt;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public double getBookingAmount() {
        return bookingAmount;
    }

    public void setBookingAmount(double bookingAmount) {
        this.bookingAmount = bookingAmount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public ShowModel getShowModel() {
        return showModel;
    }

    public void setShowModel(ShowModel showModel) {
        this.showModel = showModel;
    }


}
