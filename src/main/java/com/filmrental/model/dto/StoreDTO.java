package com.filmrental.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {

    private Integer storeId;
    private Integer managerStaffId;
    private Integer addressId;
    private LocalDateTime lastUpdate;
}
