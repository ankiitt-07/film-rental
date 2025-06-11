package com.filmrental.model.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CustomerDTO {
    private Integer customerId;
    private Integer storeId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer addressId;
    private Boolean active;
    private LocalDate createDate;
}