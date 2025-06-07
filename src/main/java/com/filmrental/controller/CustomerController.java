package com.filmrental.controller;

import com.filmrental.mapper.CustomerMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Customer;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping("/post")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO dto) {
        Optional<Store> storeOpt = storeRepository.findById(dto.storeId());
        Optional<Address> addressOpt = addressRepository.findById(dto.addressId());

        if (storeOpt.isEmpty() || addressOpt.isEmpty()) {
            return ResponseEntity.status(400).body("{\"error\": \"Invalid store or address ID\"}");
        }

        Customer customer = customerMapper.toEntity(dto);
        customer.setStore(storeOpt.get());
        customer.setAddress(addressOpt.get());
        customer.setCreateDate(dto.createDate() != null ? dto.createDate() : LocalDateTime.now());
        customer.setLastUpdate(LocalDateTime.now());

        customerRepository.save(customer);
        return ResponseEntity.status(201).body("{\"message\": \"Record Created Successfully\"}");
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByLastName(@PathVariable String ln) {
        List<Customer> customers = customerRepository.findByLastNameIgnoreCase(ln);
        if (customers.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        List<CustomerDTO> dtos = customers.stream().map(customerMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByFirstName(@PathVariable String fn) {
        List<Customer> customers = customerRepository.findByFirstNameIgnoreCase(fn);
        if (customers.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }
        List<CustomerDTO> dtos = customers.stream().map(customerMapper::toDto).toList();
        return ResponseEntity.ok(dtos);
    }
}