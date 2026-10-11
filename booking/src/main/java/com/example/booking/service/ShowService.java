package com.example.booking.service;

import com.example.booking.model.ShowModel;
import com.example.booking.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    @Autowired
    ShowRepository showRepository;

    public List<ShowModel> getAllShows() {
        return showRepository.findAll();
    }

    public ShowModel getShowById(Long id) {
        return showRepository.findById(id).orElseThrow(() -> new RuntimeException("Show not found"));
    }

    public void deleteShow(Long id){
        showRepository.deleteById(id);
    }

    public ShowModel addShow(ShowModel showModel) {
        return showRepository.save(showModel);
    }

    public ShowModel updateShow(ShowModel showModel) {
        return showRepository.save(showModel);
    }

    public List<ShowModel> getShowsByMovie(Long movieId) {
        return showRepository.findByMovieModelMovieId(movieId);
    }
}
