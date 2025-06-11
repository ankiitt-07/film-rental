package com.filmrental.mapper;

import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDTO toDto(Customer customer) {
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

    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) return null;
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setActive(dto.getActive());
        customer.setCreateDate(dto.getCreateDate());
        customer.setLastUpdate(dto.getLastUpdate());
        return customer;
    }
}