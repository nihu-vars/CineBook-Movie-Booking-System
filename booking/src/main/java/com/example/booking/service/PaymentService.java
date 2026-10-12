package com.example.booking.service;

import com.example.booking.model.BookingModel;
import com.example.booking.model.BookingSeatModel;
import com.example.booking.model.PaymentModel;
import com.example.booking.model.PaymentStatus;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.BookingSeatRepository;
import com.example.booking.repository.PaymentRepository;
import com.razorpay.*;
import jakarta.transaction.Transactional;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    BookingRepository bookingRepository;
    @Autowired
    BookingSeatRepository bookingSeatRepository;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    public PaymentModel createPayment(Long bookingId) throws Exception {
        //connect to RazorPay
        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        //getting the amount from the database
        BookingModel booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
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

    @Transactional
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

        BookingModel booking= bookingRepository.findById(payment.getBookingId()).orElseThrow(() -> new RuntimeException("Booking not found"));

        if(!"PENDING".equals(booking.getBookingStatus()) || booking.getHoldExpiresAt() == null || !booking.getHoldExpiresAt().isAfter((LocalDateTime.now()))) {
            payment.setStatus(PaymentStatus.REFUND_PENDING);
            paymentRepository.save(payment);
            initiateRefundForPending(payment.getBookingId());

            return paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(() -> new RuntimeException("Payment not found"));
        }

        List<BookingSeatModel> heldSeats = bookingSeatRepository.findByBookingModel(booking);

        if(heldSeats.isEmpty()) {
            payment.setStatus(PaymentStatus.REFUND_PENDING);
            paymentRepository.save(payment);

            initiateRefundForPending(payment.getBookingId());

            return paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(() -> new RuntimeException("Payment not found"));
        }


        Long showId = booking.getShowModel().getShowId();
        for (BookingSeatModel heldSeat : heldSeats) {
            Long seatId = heldSeat.getSeatModel().getSeatId();

            boolean alreadyBooked = bookingSeatRepository.existsConfirmedBookingForSeat(showId, seatId);

            if(alreadyBooked) {
                payment.setStatus(PaymentStatus.REFUND_PENDING);
                paymentRepository.save(payment);

                initiateRefundForPending(payment.getBookingId());
                return paymentRepository.findByRazorpayOrderId(razorpayOrderId).orElseThrow(() -> new RuntimeException("Payment not found"));
            }
        }
        booking.setBookingStatus("CONFIRMED");
        booking.setHoldExpiresAt(null);

        bookingRepository.save(booking);
        payment.setStatus(PaymentStatus.SUCCESS);
        return paymentRepository.save(payment);


    }

    @Transactional
    public void refundSuccessfulPayment(Long bookingId) throws Exception {
        PaymentModel payment = paymentRepository
                .findByBookingId(bookingId);

        if (payment == null) return;


        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            return;
        }

        String razorpayPaymentId = payment.getRazorpayPaymentId();

        if (razorpayPaymentId == null || razorpayPaymentId.isBlank()) {
            throw new IllegalStateException("Razorpay payment ID is missing");
        }

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject jsonObject = new JSONObject();

        int amountInPaise = (int) Math.round(payment.getAmount() * 100);

        jsonObject.put("amount", amountInPaise);

        try {
            Refund refund = razorpayClient.payments.refund(
                    razorpayPaymentId, jsonObject
            );

            String refundId = refund.get("id");
            String refundStatus = refund.get("status");

            payment.setRazorpayRefundId(refundId);

            if ("processed".equalsIgnoreCase(refundStatus)) {
                payment.setStatus(PaymentStatus.REFUNDED);
            } else {
                payment.setStatus(PaymentStatus.REFUND_PENDING);
            }

            paymentRepository.save(payment);

        } catch (RazorpayException e) {
            String message = e.getMessage();

            if (message != null &&
                    message.contains("The payment has been fully refunded already")) {

                payment.setStatus(PaymentStatus.REFUNDED);
                paymentRepository.save(payment);
                return;
            }

            throw e;
        }
    }

    @Transactional
    public void initiateRefundForPending(Long bookingId) throws Exception {
        PaymentModel payment = paymentRepository.findByBookingId(bookingId);

        if(payment == null) {
            throw new RuntimeException("Payment not found for booking " + bookingId);
        }

        if(payment.getRazorpayRefundId() != null && !payment.getRazorpayRefundId().isBlank()){
            return;
        }

        if(payment.getRazorpayPaymentId() == null || payment.getRazorpayPaymentId().isBlank()) {
            throw new IllegalStateException("Razorpay payment ID is missing");
        }

        RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        com.razorpay.Payment razorpayPayment = razorpayClient.payments.fetch(payment.getRazorpayPaymentId());

        if(!"captured".equalsIgnoreCase(razorpayPayment.get("status").toString())) {
            throw new IllegalStateException("Payment is not captured; refind can't be initiated yet");
        }

        JSONObject jsonObject = new JSONObject();

        JSONObject request = new JSONObject();
        int amountInPaise = (int) Math.round(payment.getAmount() * 100);
        request.put("amount", amountInPaise);

        Refund refund = razorpayClient.payments.refund(
                payment.getRazorpayPaymentId(), request);

        payment.setRazorpayRefundId(refund.get("id"));

        String refundStatus = refund.get("status");

        if ("processed".equalsIgnoreCase(refundStatus)) {
            payment.setStatus(PaymentStatus.REFUNDED);
        } else {
            payment.setStatus(PaymentStatus.REFUND_PENDING);
        }

        paymentRepository.save(payment);

    }

    @Transactional
    public void updateRefundStatus(String refundId, String refundStatus) {

        PaymentModel payment = paymentRepository
                .findByRazorpayRefundId(refundId)
                .orElseThrow(() -> new RuntimeException(
                        "Payment not found for refund " + refundId));

        if ("processed".equalsIgnoreCase(refundStatus)) {
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        }
    }
}
