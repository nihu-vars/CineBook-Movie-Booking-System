package com.example.booking.controller;
import com.example.booking.model.TheatreModel;
import com.example.booking.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class TheatreViewController {

    @Autowired
    TheatreService theatreService;

    @GetMapping("/theatres")
    public String getAllTheatres(Model model) {
        List<TheatreModel> theatreModels = theatreService.getAllTheatres();
        model.addAttribute("theatres", theatreModels);
        return "theatres";
    }

    @GetMapping("/theatres/{id}")
    public String getTheatreById(@PathVariable Long id, Model model){
        TheatreModel theatreModel = theatreService.getTheatreById(id);
        model.addAttribute("theatre", theatreModel);
        return "theatre-details";
    }

    @PostMapping("/delete/theatres/{id}")
    public String deleteTheatre(@PathVariable Long id) {
        theatreService.deleteTheatre(id);
        return "redirect:/theatres";
    }

    @GetMapping("/add")
    public String showAddTheatreForm(Model model) {
        model.addAttribute("theatre", new TheatreModel());
        return "addTheatre";
    }

    @PostMapping("/add")
    public String addTheatre(@ModelAttribute TheatreModel theatre) {
        theatreService.addTheatre(theatre);
        return "redirect:/theatres";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("theatre", theatreService.getTheatreById(id));
        return "updateTheatre";
    }

    @PostMapping("/edit/{id}")
    public String updateTheatre(@PathVariable Long id, @ModelAttribute TheatreModel theatre) {
        theatre.setTheatreId(id);
        theatreService.updateTheatre(theatre);

        return "redirect:/theatres";
    }



}
