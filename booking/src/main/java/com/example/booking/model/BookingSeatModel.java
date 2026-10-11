package com.example.booking.model;

import jakarta.persistence.*;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_show_seat",
                        columnNames = {"showId", "seatId"}
                )
        }
)
public class BookingSeatModel {


    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long bookingSeatId;
    @ManyToOne        //1 booking can have multiple seats
    @JoinColumn(name = "bookingId")
    private BookingModel bookingModel;
    @ManyToOne
    @JoinColumn(name = "seatId")
    private SeatModel seatModel;
    @ManyToOne
    @JoinColumn(name = "showId")
    private ShowModel show;

    public ShowModel getShow() {
        return show;
    }

    public void setShow(ShowModel show) {
        this.show = show;
    }


    public BookingSeatModel () {}

    public Long getBookingSeatId() {
        return bookingSeatId;
    }

    public void setBookingSeatId(Long bookingSeatId) {
        this.bookingSeatId = bookingSeatId;
    }

    public BookingModel getBookingModel() {
        return bookingModel;
    }

    public void setBookingModel(BookingModel bookingModel) {
        this.bookingModel = bookingModel;
    }

    public SeatModel getSeatModel() {
        return seatModel;
    }

    public void setSeatModel(SeatModel seatModel) {
        this.seatModel = seatModel;
    }


}
