package com.example.booking.service;

import com.example.booking.dto.OMBDMovieResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OMBDService {

    @Value("${ombd.api.key}")
    private String apiKey;

    @Value("${ombd.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate(); //this allows spring boot to make calls to ombd api

    public OMBDMovieResponse getMovieByTitle(String title) {
        String url = apiUrl + "?apiKey=" + apiKey + "&t=" + title;
        return restTemplate.getForObject(   //Make a GET request to this URL and convert the response into an OMBDMovieResponse object
                url,
                OMBDMovieResponse.class
        );

    }
}
