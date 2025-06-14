package com.filmrental.repository;

import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByCustomer_CustomerId(Integer customerId);

    @Query("SELECT r.inventory.film, COUNT(r) as rentalCount " +
            "FROM Rental r " +
            "GROUP BY r.inventory.film " +
            "ORDER BY rentalCount DESC " +
            "LIMIT 10")
    List<Object[]> findTopTenRentedFilms();

    @Query("SELECT r.inventory.film, COUNT(r) as rentalCount " +
            "FROM Rental r " +
            "WHERE r.inventory.store.storeId = :storeId " +
            "GROUP BY r.inventory.film " +
            "ORDER BY rentalCount DESC " +
            "LIMIT 10")
    List<Object[]> findTopTenRentedFilmsByStore(@Param("storeId") Integer storeId);

    @Query("SELECT DISTINCT r.customer " +
            "FROM Rental r " +
            "WHERE r.inventory.store.storeId = :storeId AND r.returnDate IS NULL")
    List<Customer> findCustomersWithDueRentalsByStore(@Param("storeId") Integer storeId);
}