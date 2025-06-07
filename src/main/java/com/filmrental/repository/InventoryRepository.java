package com.filmrental.repository;

import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Query("SELECT i.film.title, COUNT(i) FROM Inventory i GROUP BY i.film.title")

    List<Object[]> countAllFilms();

    // Inventory of a specific store
    List<Inventory> findByStoreStoreId(Long storeId);

    // Inventory of a specific film across all stores
    List<Inventory> findByFilmFilmId(Long filmId);

    // Inventory of a specific film in a specific store
    Long countByFilmFilmIdAndStoreStoreId(Long filmId, Long storeId);
}
