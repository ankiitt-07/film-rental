package com.filmrental.repository;

import com.filmrental.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findAll();

    List<Inventory> findByStoreStoreId(Integer storeId);

    List<Inventory> findByFilmFilmId(Integer filmId);

    List<Inventory> findByFilmFilmIdAndStoreStoreId(Integer filmId, Integer storeId);
}