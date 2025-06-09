package com.filmrental.mapper;

import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Store;

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

        Film film = new Film();
        film.setFilmId(dto.getFilmId());
        inventory.setFilm(film);

        Store store = new Store();
        store.setStoreId(dto.getStoreId());
        inventory.setStore(store);

        inventory.setLastUpdate(dto.getLastUpdate());
        return inventory;
    }
}