package com.filmrental.model.dto;

import lombok.Data;

@Data
public class StoreDTO {
    private Integer storeId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer addressId;
}