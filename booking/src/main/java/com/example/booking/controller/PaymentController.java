package com.example.booking.controller;

import com.example.booking.model.PaymentModel;
import com.example.booking.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping("/payment")
    public PaymentModel createOrder(@RequestParam Long bookingId) throws Exception {
        return  paymentService.createPayment(bookingId);
    }

    @PostMapping("/verifyPayment")
    public PaymentModel verifyPayment(@RequestParam String razorpayOrderId, @RequestParam String razorpayPaymentId, @RequestParam String razorpaySignature) throws Exception {
        return paymentService.verifyPayment(razorpayOrderId, razorpayPaymentId, razorpaySignature);
    }

}
