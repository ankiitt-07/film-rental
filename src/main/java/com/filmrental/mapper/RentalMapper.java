package com.filmrental.mapper;

import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.entity.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RentalMapper {
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    @Mapping(target = "staff", ignore = true)
    Rental toEntity(RentalDTO dto);

    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "inventory.inventoryId", target = "inventoryId")
    @Mapping(source = "staff.staffId", target = "staffId")
    RentalDTO toDto(Rental rental);
}