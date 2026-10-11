package com.example.booking.repository;

import com.example.booking.model.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
    //method to find a payment by razorpay order id
    Optional<PaymentModel> findByRazorpayOrderId(String razorpayOderId);
}
