package com.filmrental.model.dto;

import java.time.LocalDateTime;

public record RentalDTO(
        Integer rentalId,
        LocalDateTime rentalDate,
        LocalDateTime returnDate,
        LocalDateTime lastUpdate,
        Integer inventoryId,
        Integer customerId,
        Integer staffId
) {}
