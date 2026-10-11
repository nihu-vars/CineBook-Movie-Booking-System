package com.example.booking.service;

import com.example.booking.model.BookingModel;
import com.example.booking.model.PaymentModel;
import com.example.booking.model.PaymentStatus;
import com.example.booking.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    BookingService bookingService;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public PaymentModel createPayment(Long bookingId) throws Exception {
        //connect to RazorPay
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        //getting the amount from the database
        BookingModel booking = bookingService.getBookingById(bookingId);
        if (!"PENDING".equals(booking.getBookingStatus())) {
            throw new RuntimeException(
                    "Payment can only be initiated for a pending booking");
        }

        double amount = booking.getBookingAmount();

        JSONObject orderRequest = new JSONObject();

        orderRequest.put("amount", (int) (amount*100));  //as we are using currency INR and razorpay expects the amount in smallest unit that is paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "booking_" + bookingId);

        Order order = razorpayClient.orders.create(orderRequest);

        PaymentModel paymentModel =new PaymentModel();

        paymentModel.setBookingId(bookingId);
        paymentModel.setRazorpayOrderId(order.get("id"));
        paymentModel.setAmount(amount);
        paymentModel.setCurrency("INR");
        paymentModel.setStatus(PaymentStatus.CREATED);
        paymentModel.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(paymentModel);
    }

    public PaymentModel verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws  Exception {
        PaymentModel payment = paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(() -> new RuntimeException("Payment not found"));
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", razorpayOrderId);
        attributes.put("razorpay_payment_id", razorpayPaymentId);
        attributes.put("razorpay_signature", razorpaySignature);

        boolean isValid = Utils.verifyPaymentSignature(attributes, razorpayKeySecret);

        if(!isValid) {
            throw new RuntimeException("Invalid Payment Signature");
        }

        //fetch the actual payment details from razorpay
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        com.razorpay.Payment razorpayPayment = razorpayClient.payments.fetch(razorpayPaymentId);

        if(!razorpayOrderId.equals(razorpayPayment.get("order_id"))) {
            throw new RuntimeException("Payment does not belong to this order");
        }

        //check the amount
        int actualAmount = ((Number) razorpayPayment.get("amount")).intValue();

        int expectedAmount = (int) Math.round(payment.getAmount() * 100);

        if(actualAmount != expectedAmount) {
            throw new RuntimeException("Payment amount mismatch");
        }

        //check currency
        if (!"INR".equals(razorpayPayment.get("currency"))) {
            throw new RuntimeException("Payment currency mismatch");
        }

        String status = razorpayPayment.get("status");

        if(!"captured".equals(status)) {
            throw new RuntimeException("Payment is not captued. Current Status: " + status);

        }

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setStatus(PaymentStatus.SUCCESS);

        PaymentModel savedPayment = paymentRepository.save(payment);
        bookingService.confirmBooking(payment.getBookingId());
        return savedPayment;
    }
}
