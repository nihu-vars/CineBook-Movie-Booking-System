package com.example.booking.controller;

import com.example.booking.model.MovieModel;
import com.example.booking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MovieApiController {
    @Autowired
    MovieService movieService;

    @GetMapping("/movies")
    public List<MovieModel> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/movies/{id}")
    public MovieModel getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PutMapping("/movies/{id}")
    public MovieModel updateMovie(@RequestBody MovieModel movieModel,@PathVariable Long id) {
        movieModel.setMovieId(id);
        return movieService.updateMovie(movieModel);
    }

    @DeleteMapping("/movies/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @PostMapping("/add/{title}")
    public MovieModel importMoviesFromOmbd(@PathVariable String title) {
        return movieService.fetchMovieByTitle(title);
    }

}
