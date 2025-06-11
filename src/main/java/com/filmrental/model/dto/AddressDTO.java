package com.filmrental.model.dto;

import lombok.Data;

@Data
public class AddressDTO {
    private Integer addressId;
    private String address;
    private String address2;
    private String district;
    private Integer cityId;
    private String postalCode;
    private String phone;
}