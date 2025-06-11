package com.filmrental.model.dto;

import lombok.Data;

@Data
public class InventoryDTO {
    private Integer inventoryId;
    private Integer filmId;
    private Integer storeId;
}