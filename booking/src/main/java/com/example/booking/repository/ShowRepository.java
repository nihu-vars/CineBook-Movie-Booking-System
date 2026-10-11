package com.example.booking.repository;

import com.example.booking.model.ShowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShowRepository extends JpaRepository<ShowModel, Long> {
    List<ShowModel> findByMovieModelMovieId(Long movieId);
}
