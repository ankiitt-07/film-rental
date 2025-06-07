package com.filmrental.mapper;


import com.filmrental.model.dto.CountryDTO;
import com.filmrental.model.entity.Country;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);

    CountryDTO toDto(Country entity);
    Country toEntity(CountryDTO dto);
}
