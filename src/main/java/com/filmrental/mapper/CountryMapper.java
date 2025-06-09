package com.filmrental.mapper;

import com.filmrental.model.dto.CountryDTO;
import com.filmrental.model.entity.Country;

public class CountryMapper {

    public static CountryDTO toDto(Country country) {
        if (country == null) return null;

        return new CountryDTO(
                country.getCountryId(),
                country.getCountry(),
                country.getLastUpdate()
        );
    }

    public static Country toEntity(CountryDTO dto) {
        if (dto == null) return null;

        Country country = new Country();
        country.setCountryId(dto.getCountryId());
        country.setCountry(dto.getCountry());
        country.setLastUpdate(dto.getLastUpdate());
        return country;
    }
}