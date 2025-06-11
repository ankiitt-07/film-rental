package com.filmrental.mapper;

import com.filmrental.model.dto.CityDTO;
import com.filmrental.model.entity.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public CityDTO toDto(City city) {
        if (city == null) {
            return null;
        }
        CityDTO dto = new CityDTO();
        dto.setCityId(city.getCityId());
        dto.setCity(city.getCity());
        dto.setCountryId(city.getCountry() != null ? city.getCountry().getCountryId() : null);
        return dto;
    }

    public City toEntity(CityDTO dto) {
        if (dto == null) {
            return null;
        }
        City city = new City();
        city.setCityId(dto.getCityId());
        city.setCity(dto.getCity());
        return city;
    }
}