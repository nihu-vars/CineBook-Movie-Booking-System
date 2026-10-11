package com.example.booking.mapper;

import com.example.booking.dto.OMBDMovieResponse;
import com.example.booking.model.MovieModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MovieMapper {

    public static MovieModel toMovieModel(OMBDMovieResponse response) {

        MovieModel movie = new MovieModel();

        movie.setMovieTitle(response.getTitle());
        movie.setMovieDescription(response.getPlot());
        movie.setMovieGenre(response.getGenre());
        movie.setMovieLanguages(response.getLanguage());
        movie.setMovieDirector(response.getDirector());
        movie.setMovieWriter(response.getWriter());
        movie.setMovieActors(response.getActors());
        movie.setMovieCountry(response.getCountry());
        movie.setMovieAwards(response.getAwards());
        movie.setMoviePosterUrl(response.getPoster());
        movie.setImdbId(response.getImdbId());
        movie.setMovieImdbVotes(response.getImdbVotes());

        if (response.getImdbRating() != null
                && !response.getImdbRating().equals("N/A")) {

            movie.setMovieRating(
                    Double.parseDouble(response.getImdbRating())
            );
        }

        if (response.getRuntime() != null
                && response.getRuntime().contains("min")) {

            String runtime = response.getRuntime()
                    .replace(" min", "")
                    .trim();

            movie.setMovieDurationMinutes(
                    Integer.parseInt(runtime)
            );
        }

        if (response.getReleased() != null
                && !response.getReleased().equals("N/A")) {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd MMM yyyy");

            movie.setMovieReleaseDate(
                    LocalDate.parse(response.getReleased(), formatter)
            );
        }

        if (response.getMetascore() != null
                && !response.getMetascore().equals("N/A")) {

            movie.setMovieMetascore(
                    Integer.parseInt(response.getMetascore())
            );
        }

        return movie;
    }
}