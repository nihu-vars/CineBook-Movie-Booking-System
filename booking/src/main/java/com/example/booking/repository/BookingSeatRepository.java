package com.example.booking.repository;

import com.example.booking.model.BookingModel;
import com.example.booking.model.BookingSeatModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeatModel, Long> {
    @Query("""
    SELECT COUNT(bs) > 0
    FROM BookingSeatModel bs
    WHERE bs.bookingModel.showModel.showId = :showId
    AND bs.seatModel.seatId = :seatId
    AND bs.bookingModel.bookingStatus = 'CONFIRMED'
""")
    boolean existsConfirmedBookingForSeat(Long showId, Long seatId);

    @Query("""
    SELECT bs.seatModel.seatId
    FROM BookingSeatModel bs
    WHERE bs.bookingModel.showModel.showId = :showId
    AND bs.bookingModel.bookingStatus = 'CONFIRMED'
""")
    List<Long> findBookedSeatIdsByShowId(Long showId);

    void deleteByBookingModel(BookingModel bookingModel);

}
