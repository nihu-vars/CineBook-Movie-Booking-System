package com.example.booking.repository;

import com.example.booking.model.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Long> {
    @Query("""
    SELECT b
    FROM BookingModel b
    WHERE b.userModel.userId = :userId
""")
    List<BookingModel> findBookingByUserId(Long userId);
}
