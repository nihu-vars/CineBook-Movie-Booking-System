package com.example.booking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
public class MovieModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long movieId;

    // OMDb information
    private String imdbId;
    private String movieTitle;
    private String movieYear;
    private String movieDescription;
    private int movieDurationMinutes;
    private String movieLanguages;
    private String movieGenre;
    private double movieRating;
    private String moviePosterUrl;
    private LocalDate movieReleaseDate;

    private String movieDirector;
    private String movieWriter;
    private String movieActors;
    private String movieCountry;
    private String movieAwards;

    private int movieMetascore;
    private String movieImdbVotes;

    // Constructor
    public MovieModel() {
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public String getMovieYear() {
        return movieYear;
    }

    public void setMovieYear(String movieYear) {
        this.movieYear = movieYear;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public int getMovieDurationMinutes() {
        return movieDurationMinutes;
    }

    public void setMovieDurationMinutes(int movieDurationMinutes) {
        this.movieDurationMinutes = movieDurationMinutes;
    }

    public String getMovieLanguages() {
        return movieLanguages;
    }

    public void setMovieLanguages(String movieLanguages) {
        this.movieLanguages = movieLanguages;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public double getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(double movieRating) {
        this.movieRating = movieRating;
    }

    public String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    public void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    public LocalDate getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(LocalDate movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieDirector() {
        return movieDirector;
    }

    public void setMovieDirector(String movieDirector) {
        this.movieDirector = movieDirector;
    }

    public String getMovieWriter() {
        return movieWriter;
    }

    public void setMovieWriter(String movieWriter) {
        this.movieWriter = movieWriter;
    }

    public String getMovieActors() {
        return movieActors;
    }

    public void setMovieActors(String movieActors) {
        this.movieActors = movieActors;
    }

    public String getMovieCountry() {
        return movieCountry;
    }

    public void setMovieCountry(String movieCountry) {
        this.movieCountry = movieCountry;
    }

    public String getMovieAwards() {
        return movieAwards;
    }

    public void setMovieAwards(String movieAwards) {
        this.movieAwards = movieAwards;
    }

    public int getMovieMetascore() {
        return movieMetascore;
    }

    public void setMovieMetascore(int movieMetascore) {
        this.movieMetascore = movieMetascore;
    }

    public String getMovieImdbVotes() {
        return movieImdbVotes;
    }

    public void setMovieImdbVotes(String movieImdbVotes) {
        this.movieImdbVotes = movieImdbVotes;
    }
}