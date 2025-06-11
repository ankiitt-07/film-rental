package com.filmrental.mapper;

import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Inventory;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryDTO toDto(Inventory inventory) {
        if (inventory == null) {
            return null;
        }
        InventoryDTO dto = new InventoryDTO();
        dto.setInventoryId(inventory.getInventoryId());
        dto.setFilmId(inventory.getFilm() != null ? inventory.getFilm().getFilmId() : null);
        dto.setStoreId(inventory.getStore() != null ? inventory.getStore().getStoreId() : null);
        return dto;
    }

    public Inventory toEntity(InventoryDTO dto) {
        if (dto == null) {
            return null;
        }
        Inventory inventory = new Inventory();
        inventory.setInventoryId(dto.getInventoryId());
        return inventory;
    }
}