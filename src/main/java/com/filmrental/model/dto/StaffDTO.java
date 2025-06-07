package com.filmrental.model.dto;

import lombok.Data;

@Data
public class StaffDTO {
    private Long staff_id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private Boolean active;
    private String picture;
    private Long store_id;
    private Long address_id;
}
