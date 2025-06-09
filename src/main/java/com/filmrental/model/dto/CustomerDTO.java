package com.filmrental.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Integer customerId;
    private Integer storeId;
    private String firstName;
    private String lastName;
    private String email;
    private Integer addressId;
    private Boolean active;
    private LocalDate createDate;
    private LocalDateTime lastUpdate;
}