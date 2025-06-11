package com.filmrental.controller;

import com.filmrental.mapper.CityMapper;
import com.filmrental.model.dto.CityDTO;
import com.filmrental.model.entity.City;
import com.filmrental.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cities")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CityMapper cityMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<CityDTO>> getAllCities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<City> cities = cityRepository.findAll(pageable);
            return cities.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(cities.map(cityMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}