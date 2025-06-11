package com.filmrental.mapper;

import com.filmrental.model.dto.CountryDTO;
import com.filmrental.model.entity.Country;
import org.springframework.stereotype.Component;

@Component
public class CountryMapper {

    public CountryDTO toDto(Country country) {
        if (country == null) {
            return null;
        }
        CountryDTO dto = new CountryDTO();
        dto.setCountryId(country.getCountryId());
        dto.setCountry(country.getCountry());
        return dto;
    }

    public Country toEntity(CountryDTO dto) {
        if (dto == null) {
            return null;
        }
        Country country = new Country();
        country.setCountryId(dto.getCountryId());
        country.setCountry(dto.getCountry());
        return country;
    }
}