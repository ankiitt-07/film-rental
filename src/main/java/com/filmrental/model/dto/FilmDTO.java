package com.filmrental.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;

public record FilmDTO(
        Integer filmId,
        String title,
        String description,
        Year releaseYear,
        Integer languageId,
        Integer originalLanguageId,
        Integer rentalDuration,
        BigDecimal rentalRate,
        Integer length,
        BigDecimal replacementCost,
        String rating,
        String specialFeatures,
        LocalDateTime lastUpdate
) {}
