package com.filmrental.mapper;

import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.entity.Customer;

public class CustomerMapper {

    public static CustomerDTO toDto(Customer customer) {
        if (customer == null) return null;

        return new CustomerDTO(
                customer.getCustomerId(),
                customer.getStore() != null ? customer.getStore().getStoreId() : null,
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getAddress() != null ? customer.getAddress().getAddressId() : null,
                customer.getActive(),
                customer.getCreateDate(),
                customer.getLastUpdate()
        );
    }

    public static Customer toEntity(CustomerDTO dto) {
        if (dto == null) return null;

        Customer customer = new Customer();
        customer.setCustomerId(dto.customerId());
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());
        customer.setActive(dto.active());
        customer.setCreateDate(dto.createDate());
        customer.setLastUpdate(dto.lastUpdate());
        return customer;
    }
}