package com.filmrental.mapper;

import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDTO toAddressDTO(Address address) {
        AddressDTO dto = new AddressDTO();
        dto.setAddressId(address.getAddressId());
        dto.setAddress(address.getAddress());
        dto.setAddress2(address.getAddress2());
        dto.setDistrict(address.getDistrict());
        dto.setCityId(address.getCity().getCityId());
        dto.setPostalCode(address.getPostalCode());
        dto.setPhone(address.getPhone());
        dto.setLastUpdate(address.getLastUpdate());
        return dto;
    }

    public Address toAddressEntity(AddressDTO dto) {
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