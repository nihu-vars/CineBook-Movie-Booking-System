package com.example.booking.service;

import com.example.booking.dto.OMBDMovieResponse;
import com.example.booking.mapper.MovieMapper;
import com.example.booking.model.MovieModel;
import com.example.booking.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    OMBDService ombdService;

    public List<MovieModel> getAllMovies() {
        return movieRepository.findAll();
    }

    public MovieModel getMovieById(Long id) {
        return movieRepository.findById(id).orElse(null);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public MovieModel updateMovie(MovieModel movieModel) {
        return movieRepository.save(movieModel);
    }

    public MovieModel fetchMovieByTitle(String title) {
        OMBDMovieResponse ombdMovieResponse = ombdService.getMovieByTitle(title);
        MovieModel movieModel = MovieMapper.toMovieModel(ombdMovieResponse);
        return movieRepository.save(movieModel);
    }

}
