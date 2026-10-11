package com.example.booking.controller;

import com.example.booking.dto.OMBDMovieResponse;
import com.example.booking.service.OMBDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
public class OMBDTestController {
    @Autowired
    OMBDService ombdService;

    @GetMapping("/api/ombd/movies/{title}")
    public OMBDMovieResponse getMovies(@PathVariable String title) {
        return ombdService.getMovieByTitle(title);
    }
}
