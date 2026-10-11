package com.example.booking.repository;

import com.example.booking.model.SeatModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<SeatModel, Long> {
    //means get all seats belonging to a screen
    List<SeatModel> findByScreenModelScreenId(Long screenId);
}
