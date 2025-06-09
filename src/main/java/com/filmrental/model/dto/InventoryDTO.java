package com.filmrental.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private Integer inventoryId;
    private Integer filmId;
    private Integer storeId;
    LocalDateTime lastUpdate;

}
