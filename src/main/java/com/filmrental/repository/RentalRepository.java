package com.filmrental.repository;

import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByCustomer_CustomerId(Integer customerId);

    @Query("SELECT r.inventory.film, COUNT(r) " +
            "FROM Rental r " +
            "GROUP BY r.inventory.film " +
            "ORDER BY COUNT(r) DESC")
    Page<Object[]> findTopTenRentedFilms(Pageable pageable);

    @Query("SELECT r.inventory.film, COUNT(r) " +
            "FROM Rental r " +
            "WHERE r.inventory.store.storeId = :storeId " +
            "GROUP BY r.inventory.film " +
            "ORDER BY COUNT(r) DESC")
    Page<Object[]> findTopTenRentedFilmsByStore(@Param("storeId") Integer storeId, Pageable pageable);

    @Query("SELECT DISTINCT r.customer " +
            "FROM Rental r " +
            "WHERE r.inventory.store.storeId = :storeId AND r.returnDate IS NULL")
    List<Customer> findCustomersWithDueRentalsByStore(@Param("storeId") Integer storeId);
}