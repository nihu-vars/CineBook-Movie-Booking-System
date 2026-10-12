package com.example.booking.repository;

import com.example.booking.model.BookingModel;
import com.example.booking.model.BookingSeatModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeatModel, Long> {
    @Query("""
        SELECT COUNT(bs) > 0
        FROM BookingSeatModel bs
        WHERE bs.bookingModel.showModel.showId = :showId
          AND bs.seatModel.seatId = :seatId
          AND (
              bs.bookingModel.bookingStatus = 'CONFIRMED'
              OR (
                  bs.bookingModel.bookingStatus = 'PENDING'
                  AND bs.bookingModel.holdExpiresAt > CURRENT_TIMESTAMP
              )
          )
        """)
    boolean existsConfirmedBookingForSeat(
            @Param("showId") Long showId,
            @Param("seatId") Long seatId
    );

    @Query("""
        SELECT bs.seatModel.seatId
        FROM BookingSeatModel bs
        WHERE bs.bookingModel.showModel.showId = :showId
          AND (
              bs.bookingModel.bookingStatus = 'CONFIRMED'
              OR (
                  bs.bookingModel.bookingStatus = 'PENDING'
                  AND bs.bookingModel.holdExpiresAt > CURRENT_TIMESTAMP
              )
          )
        """)
    List<Long> findBookedSeatIdsByShowId(
            @Param("showId") Long showId
    );

    @Modifying
    @Query("""
        DELETE FROM BookingSeatModel bs
        WHERE bs.show.showId = :showId
          AND bs.bookingModel.bookingStatus = 'PENDING'
          AND bs.bookingModel.holdExpiresAt <= CURRENT_TIMESTAMP
        """)
    int deleteExpiredPendingSeatsForShow(
            @Param("showId") Long showId
    );


    void deleteByBookingModel(BookingModel bookingModel);

    List<BookingSeatModel> findByBookingModel(BookingModel bookingModel);

}
