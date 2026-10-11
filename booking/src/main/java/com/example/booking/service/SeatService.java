package com.example.booking.service;

import com.example.booking.model.ScreenModel;
import com.example.booking.model.SeatModel;
import com.example.booking.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeatService {

    @Autowired
    SeatRepository seatRepository;


    public void generateSeats(ScreenModel screenModel) {
        int totalSeats = screenModel.getTotalSeats();
        int seatsPerRow = screenModel.getSeatsPerRow();
        int regularRows = screenModel.getRegularRows();
        int premiumRows = screenModel.getPremiumRows();
        int reclinerRows = screenModel.getReclinerRows();

        int rowNumber =0;
        for(int row = 0; row<regularRows; row++) {
            createSeats(screenModel, rowNumber, seatsPerRow, "REGULAR");
            rowNumber++;
        }

        for(int row = 0; row<premiumRows; row++) {
            createSeats(screenModel, rowNumber, seatsPerRow, "PREMIUM");
            rowNumber++;
        }

        for(int row = 0; row<reclinerRows; row++) {
            createSeats(screenModel, rowNumber, seatsPerRow, "RECLINER");
            rowNumber++;
        }



    }

    public void createSeats(ScreenModel screenModel, int rowNumber, int seatsPerRow, String seatType) {

        char rowName = (char) ('A' + rowNumber);
        for(int seatNumber = 1; seatNumber<=seatsPerRow;seatNumber++) {
            SeatModel seatModel = new SeatModel();
            seatModel.setSeatNumber(rowName+String.valueOf(seatNumber));
            seatModel.setSeatType(seatType);
            seatModel.setScreenModel(screenModel);
            seatRepository.save(seatModel);

        }

    }

    public List<SeatModel> getSeatsByScreen(Long screenId) {

        return seatRepository.findByScreenModelScreenId(screenId);
    }


    public Map<Character, List<SeatModel>> getSeatLayout(Long screenId) {
        List<SeatModel> seatModels = getSeatsByScreen(screenId);
        Map<Character, List<SeatModel>> layout = new LinkedHashMap<>();
        for(SeatModel seat : seatModels){
            char row = seat.getSeatNumber().charAt(0);
            if (!layout.containsKey(row)) {
                layout.put(row, new ArrayList<>());
            }
            layout.get(row).add(seat);

        }
    return layout;
    }
}
