package com.filmrental.mapper;

import com.filmrental.model.dto.CityDTO;
import com.filmrental.model.entity.City;
import org.springframework.stereotype.Component;

@Component
public class CityMapper {

    public CityDTO toDto(City city) {
        if (city == null) return null;
        return new CityDTO(
                city.getCityId(),
                city.getCity(),
                city.getCountry() != null ? city.getCountry().getCountryId() : null,
                city.getLastUpdate()
        );
    }

    public City toEntity(CityDTO dto) {
        if (dto == null) return null;
        City city = new City();
        city.setCityId(dto.getCityId());
        city.setCity(dto.getCity());
        city.setLastUpdate(dto.getLastUpdate());
        return city;
    }
}