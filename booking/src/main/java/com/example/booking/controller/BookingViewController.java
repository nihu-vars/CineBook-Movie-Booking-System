package com.example.booking.controller;

import com.example.booking.dto.BookingRequest;
import com.example.booking.model.*;
import com.example.booking.service.BookingService;
import com.example.booking.service.SeatService;
import com.example.booking.service.ShowService;
import com.example.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class BookingViewController {

    @Autowired
    SeatService seatService;

    @Autowired
    ShowService showService;

    @Autowired
    BookingService bookingService;

    @Autowired
    UserService userService;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @GetMapping("/booking/show/{showId}")
    public String showSeatLayout(
            @PathVariable Long showId,
            Model model) {

        ShowModel show = showService.getShowById(showId);

        Long screenId = show.getScreenModel().getScreenId();

        Map<Character, List<SeatModel>> seatLayout =
                seatService.getSeatLayout(screenId);

        List<Long> bookedSeatIds =
                bookingService.getBookedSeatIds(showId);

        model.addAttribute("bookedSeatIds", bookedSeatIds);
        model.addAttribute("show", show);
        model.addAttribute("seatLayout", seatLayout);

        return "seats";
    }

    @PostMapping("/booking/create")
    @ResponseBody
    public Map<String, Object> createBooking(
            @RequestBody BookingRequest bookingRequest,
            Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException("Please log in first");
        }

        String email = authentication.getName();

        UserModel user = userService.getUserByEmail(email);

        BookingModel booking = bookingService.createBooking(
                bookingRequest.getShowId(),
                user.getUserId(),
                bookingRequest.getSeatIds()
        );

        return Map.of("bookingId", booking.getBookingId());
    }

    @GetMapping("/booking/payment/{bookingId}")
    public String showPaymentPage(
            @PathVariable Long bookingId,
            Authentication authentication,
            Model model) {

        if (authentication == null) {
            throw new RuntimeException("Please log in first");
        }

        String email = authentication.getName();

        UserModel user = userService.getUserByEmail(email);

        BookingModel booking =
                bookingService.getBookingById(bookingId);

        if (!booking.getUserModel().getUserId()
                .equals(user.getUserId())) {
            throw new RuntimeException(
                    "You cannot access this booking");
        }

        if (!"PENDING".equals(booking.getBookingStatus())) {
            throw new RuntimeException(
                    "This booking is not awaiting payment");
        }

        model.addAttribute("booking", booking);
        model.addAttribute("razorpayKeyId", razorpayKeyId);

        return "payment";
    }

    @GetMapping("/user/bookings")
    public String getUserBookings(
            Authentication authentication,
            Model model) {

        String email = authentication.getName();

        UserModel user = userService.getUserByEmail(email);

        List<BookingModel> bookings =
                bookingService.getTheBookingsOfTheUser(
                        user.getUserId());

        model.addAttribute("bookings", bookings);

        return "user/bookings";
    }

    @PostMapping("/user/bookings/{bookingId}/cancel")
    public String cancelBooking(
            Authentication authentication,
            @PathVariable Long bookingId) {

        String email = authentication.getName();

        UserModel user = userService.getUserByEmail(email);

        bookingService.cancelBooking(
                bookingId,
                user.getUserId());

        return "redirect:/user/bookings";
    }

    @GetMapping("/bookings")
    public String getAllTheBookings(Model model) {

        List<BookingModel> bookingModels =
                bookingService.getAllBookings();

        model.addAttribute("bookings", bookingModels);

        return "bookings";
    }

    @PostMapping("/bookings/{bookingId}/cancel")
    public String cancelBookingByAdmin(
            @PathVariable Long bookingId) {

        bookingService.cancelBookingByAdmin(bookingId);

        return "redirect:/bookings";
    }
}
