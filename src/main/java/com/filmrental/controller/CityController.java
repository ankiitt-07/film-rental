package com.filmrental.controller;


import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.City;

import com.filmrental.repository.CityRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers/city")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private CustomerMapper customerMapper;

    // Get stores by city name
    @GetMapping("/{city}/stores")
    public ResponseEntity<List<StoreDTO>> getStoresByCity(@PathVariable String city) {
        City cityEntity = cityRepository.findByCity(city)
                .orElseThrow(() -> new RuntimeException("City not found: " + city));

        List<StoreDTO> stores = storeRepository.findByAddressCity(cityEntity)
                .stream()
                .map(storeMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(stores);
    }

    // Get staff by city name
    @GetMapping("/{city}/staff")
    public ResponseEntity<List<StaffDTO>> getStaffByCity(@PathVariable String city) {
        City cityEntity = cityRepository.findByCity(city)
                .orElseThrow(() -> new RuntimeException("City not found: " + city));

        List<StaffDTO> staff = staffRepository.findByAddressCity(cityEntity)
                .stream()
                .map(staffMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(staff);
    }

    // Get customers by city name
    @GetMapping("/{city}/customers")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCity(@PathVariable String city) {
        City cityEntity = cityRepository.findByCity(city)
                .orElseThrow(() -> new RuntimeException("City not found: " + city));

        List<CustomerDTO> customers = customerRepository.findByAddressCity(cityEntity)
                .stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(customers);
    }
}
