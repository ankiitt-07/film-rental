package com.filmrental.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;


public record RentalDTO(Integer rentalId, LocalDateTime rentalDate, LocalDateTime returnDate,
                        LocalDateTime lastUpdate) implements Serializable {
}
