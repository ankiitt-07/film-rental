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
import com.filmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/country")
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

    @GetMapping("/{country}/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCountry(@PathVariable String country) {
        try {
            validateCountryName(country);
            Country countryEntity = getCountryEntity(country);
            List<CustomerDTO> customers = customerRepository.findByAddressCityCountry(countryEntity)
                    .stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());

            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customers by country: " + country, e);
        }
    }

    @GetMapping("/{country}/staff")
    public ResponseEntity<List<StaffDTO>> getStaffByCountry(@PathVariable String country) {
        try {
            validateCountryName(country);
            Country countryEntity = getCountryEntity(country);
            List<StaffDTO> staff = staffRepository.findByAddressCityCountry(countryEntity)
                    .stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());

            if (staff.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff by country: " + country, e);
        }
    }

    @GetMapping("/{country}/stores")
    public ResponseEntity<List<StoreDTO>> getStoresByCountry(@PathVariable String country) {
        try {
            validateCountryName(country);
            Country countryEntity = getCountryEntity(country);
            List<StoreDTO> stores = storeRepository.findByAddressCityCountry(countryEntity)
                    .stream()
                    .map(storeMapper::toDto)
                    .collect(Collectors.toList());

            if (stores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            return ResponseEntity.ok(stores);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching stores by country: " + country, e);
        }
    }

    private void validateCountryName(String country) {
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid country: country name cannot be empty");
        }
    }

    private Country getCountryEntity(String country) {
        return countryRepository.findByCountryIgnoreCase(country.trim())
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + country));
    }
}
