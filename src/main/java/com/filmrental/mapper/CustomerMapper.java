package com.filmrental.mapper;

import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.entity.Customer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CustomerMapper {

    public CustomerDTO toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setStoreId(customer.getStore() != null ? customer.getStore().getStoreId() : null);
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setEmail(customer.getEmail());
        dto.setAddressId(customer.getAddress() != null ? customer.getAddress().getAddressId() : null);
        dto.setActive(customer.getActive());
        dto.setCreateDate(LocalDate.from(customer.getCreateDate()));
        return dto;
    }

    public Customer toEntity(CustomerDTO dto) {
        if (dto == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setCustomerId(dto.getCustomerId());
        customer.setFirstName(dto.getFirstName());
        customer.setLastName(dto.getLastName());
        customer.setEmail(dto.getEmail());
        customer.setActive(dto.getActive());
        customer.setCreateDate(dto.getCreateDate().atStartOfDay());
        return customer;
    }
}