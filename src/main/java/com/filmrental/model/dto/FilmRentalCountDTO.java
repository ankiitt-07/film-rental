package com.filmrental.model.dto;

import java.io.Serializable;

public record FilmRentalCountDTO (
        Integer filmId,
        String title,
        Long rentalCount
) implements Serializable {
}
