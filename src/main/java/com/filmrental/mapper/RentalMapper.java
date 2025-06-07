package com.filmrental.mapper;

import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.entity.Rental;

public class RentalMapper {
    // Convert Entity → DTO
    public static RentalDTO toDto(Rental rental) {
        if (rental == null) return null;

        return new RentalDTO(
                rental.getRentalId(),
                rental.getRentalDate(),
                rental.getReturnDate(),
                rental.getLastUpdate()
        );
    }

    // Convert DTO → Entity (partial — does not map relationships)
    public static Rental toEntity(RentalDTO dto) {
        if (dto == null) return null;

        Rental rental = new Rental();
        rental.setRentalId(dto.rentalId());
        rental.setRentalDate(dto.rentalDate());
        rental.setReturnDate(dto.returnDate());
        rental.setLastUpdate(dto.lastUpdate());

        // Relationships like inventory, customer, staff need to be set manually
        return rental;
    }
}
