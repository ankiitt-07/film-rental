package com.filmrental.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.Year;
import java.util.List;

@Data
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
    private List<Integer> actorIds;
    private List<Integer> categoryIds;
}