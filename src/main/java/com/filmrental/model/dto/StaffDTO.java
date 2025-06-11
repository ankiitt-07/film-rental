package com.filmrental.model.dto;

import lombok.Data;

@Data
public class StaffDTO {
    private Integer staffId;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean active;
    private String picture;
    private String username;
    private String password;
    private Integer storeId;
    private Integer addressId;
}