package com.filmrental.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO {
    private Integer cityId;
    private String city;
    private Integer countryId;
    private LocalDateTime lastUpdate;
}

