package com.filmrental.mapper;


import com.filmrental.model.dto.CityDTO;
import com.filmrental.model.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    @Mapping(source = "country.countryId", target = "countryId")
    CityDTO toDto(City entity);

    @Mapping(source = "countryId", target = "country.countryId")
    City toEntity(CityDTO dto);
}

