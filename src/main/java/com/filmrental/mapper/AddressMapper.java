package com.filmrental.mapper;

import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO toDto(Address address) {
        if (address == null) {
            return null;
        }
        AddressDTO dto = new AddressDTO();
        dto.setAddressId(address.getAddressId());
        dto.setAddress(address.getAddress());
        dto.setAddress2(address.getAddress2());
        dto.setDistrict(address.getDistrict());
        dto.setCityId(address.getCity() != null ? address.getCity().getCityId() : null);
        dto.setPostalCode(address.getPostalCode());
        dto.setPhone(address.getPhone());
        return dto;
    }

    public Address toEntity(AddressDTO dto) {
        if (dto == null) {
            return null;
        }
        Address address = new Address();
        address.setAddressId(dto.getAddressId());
        address.setAddress(dto.getAddress());
        address.setAddress2(dto.getAddress2());
        address.setDistrict(dto.getDistrict());
        address.setPostalCode(dto.getPostalCode());
        address.setPhone(dto.getPhone());
        return address;
    }
}