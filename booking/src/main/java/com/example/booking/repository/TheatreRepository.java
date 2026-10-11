package com.example.booking.repository;

import com.example.booking.model.TheatreModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreRepository extends JpaRepository<TheatreModel, Long> {
}
