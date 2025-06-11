package com.filmrental.controller;

import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.City;
import com.filmrental.repository.CityRepository;
import com.filmrental.repository.StaffRepository;
import com.filmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private StoreMapper storeMapper;

    @Autowired
    private StaffMapper staffMapper;

    // Get stores by city name
    @GetMapping("/{city}/stores")
    public ResponseEntity<List<StoreDTO>> getStoresByCity(@PathVariable("city") String city) {
        try {
            City cityEntity = cityRepository.findByCity(city)
                    .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));

            List<StoreDTO> stores = storeRepository.findByAddressCity(cityEntity)
                    .stream()
                    .map(storeMapper::toDto)
                    .collect(Collectors.toList());

            if (stores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(stores);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching stores in city: " + city);
        }
    }

    // Get staff by city name
    @GetMapping("/{city}/staff")
    public ResponseEntity<List<StaffDTO>> getStaffByCity(@PathVariable("city") String city) {
        try {
            City cityEntity = cityRepository.findByCity(city)
                    .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));

            List<StaffDTO> staff = staffRepository.findByAddressCity(cityEntity)
                    .stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());

            if (staff.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(staff);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff in city: " + city);
        }
    }
}