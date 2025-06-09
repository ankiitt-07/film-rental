package com.filmrental.mapper;

import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.entity.Rental;

public class RentalMapper {

    public static RentalDTO toDto(Rental rental) {
        if (rental == null) return null;

        return new RentalDTO(
                rental.getRentalId(),
                rental.getRentalDate(),
                rental.getReturnDate(),
                rental.getLastUpdate(),
                rental.getInventory() != null ? rental.getInventory().getInventoryId() : null,
                rental.getCustomer() != null ? rental.getCustomer().getCustomerId() : null,
                rental.getStaff() != null ? rental.getStaff().getStaffId() : null
        );
    }

    public static Rental toEntity(RentalDTO dto) {
        if (dto == null) return null;

        Rental rental = new Rental();
        rental.setRentalId(dto.rentalId());
        rental.setRentalDate(dto.rentalDate());
        rental.setReturnDate(dto.returnDate());
        rental.setLastUpdate(dto.lastUpdate());
        return rental;
    }
}