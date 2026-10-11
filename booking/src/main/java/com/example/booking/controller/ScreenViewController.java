package com.example.booking.controller;

import com.example.booking.model.ScreenModel;
import com.example.booking.model.TheatreModel;
import com.example.booking.repository.ScreenRepository;
import com.example.booking.service.ScreenService;
import com.example.booking.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;


@Controller
public class ScreenViewController {
    @Autowired
    TheatreService theatreService;
    @Autowired
    ScreenService screenService;

    @GetMapping("/screens")
    public String getAllScreens(Model model) {
        List <ScreenModel> screens = screenService.getAllScreens();
        model.addAttribute("screens", screens);
        return "screens";
    }

    @GetMapping("/screens/{id}")
    public String getScreenById(@PathVariable Long id, Model model) {
        ScreenModel screenModel = screenService.getScreenById(id);
        model.addAttribute("screen", screenModel);
        return "screen-details";

    }

    @PostMapping("/screens/delete/{id}")
    public String deleteScreen(@PathVariable Long id) {
        screenService.deleteScreen(id);
        return "redirect:/screens";
    }


    @GetMapping("/screen/add")
    public String showAddScreenForm(Model model) {
        model.addAttribute("screen", new ScreenModel());
        model.addAttribute("theaters", theatreService.getAllTheatres());
        return "addScreen";
    }

    @PostMapping("/screen/add")
    public String addScreen(@ModelAttribute ScreenModel screenModel) {
        screenService.addScreen(screenModel);
        return "redirect:/screens";
    }

    @GetMapping("/screen/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("screen", screenService.getScreenById(id));
        model.addAttribute("theaters", theatreService.getAllTheatres());
        return "updateScreen";
    }

    @PostMapping("/screen/edit/{id}")
    public String updateScreen(@PathVariable Long id, @ModelAttribute ScreenModel screenModel) {
        screenModel.setScreenId(id);
        screenService.updateScreen(screenModel);

        return "redirect:/screens";
    }


}
