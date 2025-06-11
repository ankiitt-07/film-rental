package com.filmrental.model.dto;

import lombok.Data;

@Data
public class CityDTO {
    private Integer cityId;
    private String city;
    private Integer countryId;
}