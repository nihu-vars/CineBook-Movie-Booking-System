package com.example.booking.service;

import com.example.booking.model.ScreenModel;
import com.example.booking.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenService {

    @Autowired
    ScreenRepository screenRepository;
    @Autowired
    SeatService seatService;

    public List<ScreenModel> getAllScreens() {
        return screenRepository.findAll();
    }

    public ScreenModel getScreenById(Long id) {
        return screenRepository.findById(id).orElseThrow(() -> new RuntimeException("Screen Not Found"));
    }

    public void deleteScreen(Long id) {
        screenRepository.deleteById(id);
    }

    public ScreenModel addScreen(ScreenModel ScreenModel) {
        ScreenModel savedScreen = screenRepository.save(ScreenModel);
        seatService.generateSeats(savedScreen);   //generating the seats when the screen is created
        return savedScreen;
    }

    public ScreenModel updateScreen(ScreenModel ScreenModel) {
        return screenRepository.save(ScreenModel);
    }


}
