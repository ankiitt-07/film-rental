package com.filmrental.controller;


import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.Country;
import com.filmrental.repository.CountryRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CountryController {

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private StoreMapper storeMapper;

    // Get customers by country
    @GetMapping("/api/customers/country/{country}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCountry(@PathVariable String country) {
        Country countryEntity = countryRepository.findByCountry(country)
                .orElseThrow(() -> new RuntimeException("Country not found: " + country));

        List<CustomerDTO> customers = customerRepository.findByAddressCityCountry(countryEntity)
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(customers);
    }

    // Get staff by country
    @GetMapping("/api/staff/country/{country}")
    public ResponseEntity<List<StaffDTO>> getStaffByCountry(@PathVariable String country) {
        Country countryEntity = countryRepository.findByCountry(country)
                .orElseThrow(() -> new RuntimeException("Country not found: " + country));

        List<StaffDTO> staff = staffRepository.findByAddressCityCountry(countryEntity)
                .stream()
                .map(staffMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(staff);
    }

    // Get stores by country
    @GetMapping("/api/store/country/{country}")
    public ResponseEntity<List<StoreDTO>> getStoresByCountry(@PathVariable String country) {
        Country countryEntity = countryRepository.findByCountry(country)
                .orElseThrow(() -> new RuntimeException("Country not found: " + country));

        List<StoreDTO> stores = storeRepository.findByAddressCityCountry(countryEntity)
                .stream()
                .map(storeMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(stores);
    }
}
