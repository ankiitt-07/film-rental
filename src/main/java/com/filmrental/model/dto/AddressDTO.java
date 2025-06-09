package com.filmrental.model.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddressDTO {
    private Integer addressId;
    private String address;
    private String address2;
    private String district;
    private Short cityId;
    private String postalCode;
    private String phone;
    private LocalDateTime lastUpdate;
}
