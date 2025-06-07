package com.filmrental.mapper;

import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StaffMapper {
    @Mapping(source = "staff_id", target = "staff_id")
    @Mapping(source = "first_name", target = "first_name")
    @Mapping(source = "last_name", target = "last_name")
    @Mapping(source = "store.store_id", target = "store_id")
    @Mapping(source = "address.address_id", target = "address_id")
    StaffDTO staffToDTO(Staff staff);

    @Mapping(source = "staff_id", target = "staff_id")
    @Mapping(source = "first_name", target = "first_name")
    @Mapping(source = "last_name", target = "last_name")
    @Mapping(source = "store_id", target = "store.store_id")
    @Mapping(source = "address_id", target = "address.address_id")
    Staff DTOToStaff(StaffDTO staffDTO);

    @Mapping(source = "city.city", target = "city")
    @Mapping(source = "city.country.country", target = "country")
    @Mapping(source = "postal_code", target = "postal_code")
    AddressDTO addressToDTO(Address address);
}