package com.filmrental.mapper;

import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.entity.City;
import com.filmrental.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(source = "store.storeId", target = "storeId")
    @Mapping(source = "address.addressId", target = "addressId")
    CustomerDTO toDto(Customer entity);

    @Mapping(source = "storeId", target = "store.storeId")
    @Mapping(source = "addressId", target = "address.addressId")
    Customer toEntity(CustomerDTO dto);

}