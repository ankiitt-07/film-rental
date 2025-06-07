package com.filmrental.mapper;

import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Store;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InventoryMapper {

    public Inventory toEntity(InventoryDTO dto, Film film, Store store) {
        Inventory inventory = new Inventory();
        inventory.setFilm(film);
        inventory.setStore(store);
        inventory.setLastUpdate(LocalDateTime.now());
        return inventory;
    }
}