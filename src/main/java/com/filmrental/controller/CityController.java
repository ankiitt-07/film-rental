package com.filmrental.controller;

import com.filmrental.model.dto.CityDTO;
import com.filmrental.model.dto.PageDTO;
import com.filmrental.model.entity.City;
import com.filmrental.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    @GetMapping
    public PageDTO<CityDTO> getAllCities(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<City> pageResult = cityRepository.findAll(pageable);
        PageDTO<CityDTO> pageDTO = new PageDTO<>();
        pageDTO.setContent(pageResult.getContent().stream()
                .map(city -> {
                    CityDTO dto = new CityDTO();
                    dto.setCityId(city.getCityId());
                    dto.setCity(city.getCity());
                    dto.setCountryId(city.getCountry() != null ? city.getCountry().getCountryId() : null); // Fixed mapping
                    return dto;
                }).toList());
        pageDTO.setNumber(pageResult.getNumber());
        pageDTO.setSize(pageResult.getSize());
        pageDTO.setTotalElements(pageResult.getTotalElements());
        pageDTO.setTotalPages(pageResult.getTotalPages());
        return pageDTO;
    }
}