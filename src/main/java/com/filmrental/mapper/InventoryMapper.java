package com.filmrental.mapper;

import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Inventory;

public class InventoryMapper {

    public static InventoryDTO toDto(Inventory inventory) {
        if (inventory == null) return null;

        return new InventoryDTO(
                inventory.getInventoryId(),
                inventory.getFilm() != null ? inventory.getFilm().getFilmId() : null,
                inventory.getStore() != null ? inventory.getStore().getStoreId() : null,
                inventory.getLastUpdate()
        );
    }

    public static Inventory toEntity(InventoryDTO dto) {
        if (dto == null) return null;

        Inventory inventory = new Inventory();
        inventory.setInventoryId(dto.getInventoryId());
        inventory.setLastUpdate(dto.getLastUpdate());
        return inventory;
    }
}