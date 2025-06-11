package com.filmrental.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmDTO {
    private Integer filmId;
    private String title;
    private String description;
    private Year releaseYear;
    private Integer languageId;
    private Integer originalLanguageId;
    private Integer rentalDuration;
    private BigDecimal rentalRate;
    private Integer length;
    private BigDecimal replacementCost;
    private String rating;
    private String specialFeatures;
    private LocalDateTime lastUpdate;
    private List<ActorDTO> actors; // Added to include actors
}