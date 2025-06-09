package com.filmrental.model.dto;

import lombok.Data;

@Data
public class StaffDTO {
    private Integer staffId;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private Boolean active;
    private String picture;
    private Integer storeId;
    private String userName;
    private String password;
    private Integer addressId;
}