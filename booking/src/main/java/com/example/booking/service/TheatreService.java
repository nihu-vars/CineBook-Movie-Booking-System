package com.example.booking.service;

import com.example.booking.model.TheatreModel;
import com.example.booking.repository.TheatreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    @Autowired
    TheatreRepository theatreRepository;

    public List<TheatreModel> getAllTheatres() {
        return theatreRepository.findAll();
    }

    public TheatreModel getTheatreById(Long id) {
        return theatreRepository.findById(id).orElseThrow(() -> new RuntimeException("Theatre Not Found"));
    }

    public void deleteTheatre(Long id) {
        theatreRepository.deleteById(id);
    }

    public TheatreModel addTheatre(TheatreModel theatreModel) {
        return theatreRepository.save(theatreModel);
    }

    public TheatreModel updateTheatre(TheatreModel theatreModel) {
        return theatreRepository.save(theatreModel);
    }


}
