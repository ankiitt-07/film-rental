package com.filmrental.repository;

import com.filmrental.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByFilm_FilmId(Integer filmId);
    List<Inventory> findByStore_StoreId(Integer storeId);
}