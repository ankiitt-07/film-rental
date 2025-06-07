package com.filmrental.model.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryDTO {
    private Long filmId;
    private Long storeId;
}
