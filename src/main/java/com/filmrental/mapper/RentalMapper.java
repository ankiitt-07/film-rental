package com.filmrental.mapper;

import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.entity.Rental;
import org.springframework.stereotype.Component;

@Component
public class RentalMapper {

    public RentalDTO toDto(Rental rental) {
        if (rental == null) {
            return null;
        }
        RentalDTO dto = new RentalDTO();
        dto.setRentalId(rental.getRentalId());
        dto.setRentalDate(rental.getRentalDate());
        dto.setInventoryId(rental.getInventory() != null ? rental.getInventory().getInventoryId() : null);
        dto.setCustomerId(rental.getCustomer() != null ? rental.getCustomer().getCustomerId() : null);
        dto.setReturnDate(rental.getReturnDate());
        dto.setStaffId(rental.getStaff() != null ? rental.getStaff().getStaffId() : null);
        return dto;
    }

    public Rental toEntity(RentalDTO dto) {
        if (dto == null) {
            return null;
        }
        Rental rental = new Rental();
        rental.setRentalId(dto.getRentalId());
        rental.setRentalDate(dto.getRentalDate());
        rental.setReturnDate(dto.getReturnDate());
        return rental;
    }
}