package com.example.booking.service;

import com.example.booking.model.*;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.BookingSeatRepository;
import com.example.booking.repository.SeatRepository;
import com.example.booking.repository.ShowRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BookingSeatRepository bookingSeatRepository;
    @Autowired
    SeatRepository seatRepository;
    @Autowired
    ShowRepository showRepository;
    @Autowired
    ShowService showService;
    @Autowired
    UserService userService;


    @Transactional   //so we are maing it transactional so that if two users try to book the same seats at the same time then that race condition should be resolved
    public BookingModel createBooking(Long showId, Long userId, List<Long> seatIds) {
        ShowModel showModel = showService.getShowById(showId);
        List<SeatModel> seatModels = seatRepository.findAllById(seatIds);

        UserModel userModel = userService.getUserById(userId);

        //checking if number of seats in seatModels equal to the value passed in Long list
        if (seatModels.size() != seatIds.size()) {
            throw new RuntimeException("One or more seats not found");
        }

        for (SeatModel seats : seatModels) {
            boolean seatAvailability = bookingSeatRepository.existsConfirmedBookingForSeat(showId, seats.getSeatId()); //for booking seats we are checking that the booking is confirmed
            if (seatAvailability) {
                throw new RuntimeException("Seat is already booked");
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double amount = 0;
        for (SeatModel seatModel : seatModels) {
            amount += showModel.getPrice();
        }

        BookingModel booking = new BookingModel();

        booking.setUserModel(userModel);
        booking.setShowModel(showModel);
        booking.setBookingDate(LocalDate.now());
        booking.setBookingAmount(amount);
        booking.setBookingStatus("PENDING");

        booking = bookingRepository.save(booking);

        for (SeatModel seat : seatModels) {

            BookingSeatModel bookingSeat = new BookingSeatModel();

            bookingSeat.setBookingModel(booking);
            bookingSeat.setSeatModel(seat);
            bookingSeat.setShow(showModel);

            try {
                bookingSeatRepository.saveAndFlush(bookingSeat);
            } catch (DataIntegrityViolationException e) {

                throw new RuntimeException(
                        "Sorry, this seat was just booked by another user."
                );
            }
        }

        return booking;
    }

    //to make the seats booked as shows in different colour
    public List<Long> getBookedSeatIds(Long showId) {
        return bookingSeatRepository.findBookedSeatIdsByShowId(showId);
    }

    public List<BookingModel> getTheBookingsOfTheUser(Long userId) {
        return bookingRepository.findBookingByUserId(userId);

    }
    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        BookingModel booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        LocalDate today = LocalDate.now();
        ShowModel show = booking.getShowModel();
        LocalDate showDate = show.getShowDate();

        if (!booking.getUserModel().getUserId().equals(userId)) {
            throw new RuntimeException("You cannot cancel this booking");
        }

        //cant cancel booking after the show date
        if (today.isAfter(showDate)) {
            throw new RuntimeException("You can not cancel the booking after the show");
        }

        // cant cancel booking on the show date
        if (today.isEqual(showDate)) {
            throw new RuntimeException("You cannot cancel this booking on the show date");
        }
        bookingSeatRepository.deleteByBookingModel(booking);  //deleting the seat if the booking has been cancelled so that the other user can book the seat

        booking.setBookingStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    public List<BookingModel> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public void cancelBookingByAdmin(Long bookingId) {

        BookingModel booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        bookingSeatRepository.deleteByBookingModel(booking);

        booking.setBookingStatus("CANCELLED");

        bookingRepository.save(booking);
    }

    //method to confirm the booking after the payment
    @Transactional
    public BookingModel confirmBooking(Long bookingId) {
        BookingModel bookingModel = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if("CONFIRMED".equals(bookingModel.getBookingStatus())) {
            return bookingModel;
        }

        if(!"PENDING".equals(bookingModel.getBookingStatus())) {
            throw new RuntimeException("Only pending bookings can be confirmed");

        }

        bookingModel.setBookingStatus("CONFIRMED");
        return bookingRepository.save(bookingModel);
    }

    public BookingModel getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() ->
                        new RuntimeException("Booking not found"));
    }
}
