package com.filmrental.mapper;

import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.entity.Address;

public class AddressMapper {

    public static AddressDTO toDto(Address address) {
        if (address == null) return null;

        return new AddressDTO(
                address.getAddressId(),
                address.getAddress(),
                address.getAddress2(),
                address.getDistrict(),
                address.getCity() != null ? address.getCity().getCityId().shortValue() : null,
                address.getPostalCode(),
                address.getPhone(),
                address.getLastUpdate()
        );
    }

    public static Address toEntity(AddressDTO dto) {
        if (dto == null) return null;

        Address address = new Address();
        address.setAddressId(dto.getAddressId());
        address.setAddress(dto.getAddress());
        address.setAddress2(dto.getAddress2());
        address.setDistrict(dto.getDistrict());
        address.setPostalCode(dto.getPostalCode());
        address.setPhone(dto.getPhone());
        address.setLastUpdate(dto.getLastUpdate());
        return address;
    }
}