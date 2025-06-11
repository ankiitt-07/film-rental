package com.filmrental.repository;

import com.filmrental.model.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    @Query("SELECT i.film.title, COUNT(i) FROM Inventory i GROUP BY i.film.title")
    List<Object[]> findInventoryCountByFilm();

    @Query("SELECT i.film.title, COUNT(i) FROM Inventory i WHERE i.store.storeId = :storeId GROUP BY i.film.title")
    List<Object[]> findInventoryCountByStore(@Param("storeId") Integer storeId);

    @Query("SELECT i.store.address.address, COUNT(i) FROM Inventory i WHERE i.film.filmId = :filmId GROUP BY i.store.address.address")
    List<Object[]> findInventoryCountByFilmId(@Param("filmId") Integer filmId);

    @Query("SELECT i.store.address.address, COUNT(i) FROM Inventory i WHERE i.film.filmId = :filmId AND i.store.storeId = :storeId GROUP BY i.store.address.address")
    Object[] findInventoryCountByFilmIdAndStore(@Param("filmId") Integer filmId, @Param("storeId") Integer storeId);
}