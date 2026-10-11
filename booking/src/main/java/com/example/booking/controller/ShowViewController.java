package com.example.booking.controller;

import com.example.booking.model.MovieModel;
import com.example.booking.model.ScreenModel;
import com.example.booking.model.ShowModel;
import com.example.booking.service.MovieService;
import com.example.booking.service.ScreenService;
import com.example.booking.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ShowViewController {

    @Autowired
    ShowService showService;
    @Autowired
    MovieService movieService;
    @Autowired
    ScreenService screenService;

    @GetMapping("/shows")
    public String getAllShows(Model model) {
        List<ShowModel> shows = showService.getAllShows();
        model.addAttribute("shows", shows);
        return "shows";
    }

    @GetMapping("/shows/{id}")
    public String getShowById(@PathVariable Long id, Model model) {
        ShowModel showModel = showService.getShowById(id);
        model.addAttribute("show", showModel);
        return "show-details";
    }

    @PostMapping("/shows/delete/{id}")
    public String deleteShow(@PathVariable Long id) {
        showService.deleteShow(id);
        return "redirect:/shows";
    }

    @GetMapping("/shows/add")
    public String showAddShowForm(Model model) {
        model.addAttribute("show", new ShowModel());
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("screens", screenService.getAllScreens());
        return "addShow";
    }

    @PostMapping("/shows/add")
    public String addShow(@ModelAttribute ShowModel showModel) {
        showService.addShow(showModel);
        return "redirect:/shows";
    }

    @GetMapping("/shows/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("show", showService.getShowById(id));
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("screens", screenService.getAllScreens());

        return "updateShow";
    }

    @PostMapping("/shows/edit/{id}")
    public String updateShow(@PathVariable Long id, @ModelAttribute ShowModel showModel) {
        showModel.setShowId(id);
        showService.updateShow(showModel);
        return "redirect:/shows";
    }

    @GetMapping("/shows/movie/{movieId}")
    public String getShowsOfMovie(@PathVariable Long movieId, Model model) {

        MovieModel movieModel = movieService.getMovieById(movieId);
        List<ShowModel> shows = showService.getShowsByMovie(movieId);

        model.addAttribute("movie", movieModel);
        model.addAttribute("shows", shows);
        return "movie-shows";

    }


}
