package com.example.booking.repository;

import com.example.booking.model.ScreenModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepository extends JpaRepository<ScreenModel, Long> {
}
