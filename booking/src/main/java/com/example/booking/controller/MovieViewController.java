package com.example.booking.controller;

import com.example.booking.model.MovieModel;
import com.example.booking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.*;

@Controller
public class MovieViewController {

    @Autowired
    MovieService movieService;

    @GetMapping("/user/movies")
    public String getAllMoviesUser(Model model) {
        List<MovieModel> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "/user/movies";  // the thing through we are returning the String here is because thymeleaf

    }

    @GetMapping("/movies")
    public String getAllMoviesAdmin(Model model) {
        List<MovieModel> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "/movies";  // the thing through we are returning the String here is because thymeleaf

    }

    @GetMapping("/user/movies/{id}")
    public String getMovieById(Model model, @PathVariable Long id) {
        MovieModel movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "/movie-details";
    }

    @GetMapping("/admin/movies/{id}")
    public String getMovieByIdAdmin(Model model, @PathVariable Long id) {
        MovieModel movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "/admin-movie-details";
    }

    @PostMapping("/movies/{id}/delete")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies";
    }

    @GetMapping("/movies/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        MovieModel movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "updateMovie";

    }

    @PostMapping("/movies/update")
    public String updateMovie(@ModelAttribute MovieModel movieModel) {
        movieService.updateMovie(movieModel);
        return "redirect:/movies";
    }

    @GetMapping("/movies/add")
    public String showAddMovieForm() {
        return "addMovie";
    }


    @PostMapping("/movies/add")
    public String saveMovie(@RequestParam String title) {
        movieService.fetchMovieByTitle(title);
        return "redirect:/movies";
    }

}
