package com.filmrental.repository;

import com.filmrental.model.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {

    // For GET /api/rental/customer/{id} — Find rentals by customer ID
    List<Rental> findByCustomerCustomerId(Integer customerId);

    // For GET /api/rental/due/store/{id} — Find rentals that are due (return_date is null and rental_date is past due)
    @Query("SELECT r FROM Rental r WHERE r.inventory.store.storeId = :storeId " +
            "AND r.returnDate IS NULL " +
            "AND r.rentalDate < :dueDateThreshold")
    List<Rental> findDueRentalsByStoreId(@Param("storeId") Integer storeId,
                                         @Param("dueDateThreshold") java.time.LocalDateTime dueDateThreshold);

    // For GET /api/rental/toptenfilms — Top 10 rented films (across all stores)
    @Query(value = "SELECT r.inventory.film.filmId, r.inventory.film.title, COUNT(r.rentalId) as rentalCount " +
            "FROM Rental r " +
            "GROUP BY r.inventory.film.filmId, r.inventory.film.title " +
            "ORDER BY rentalCount DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTopTenRentedFilms();

    // For GET /api/rental/toptenfilms/store/{id} — Top 10 rented films for a specific store
    @Query(value = "SELECT r.inventory.film.filmId, r.inventory.film.title, COUNT(r.rentalId) as rentalCount " +
            "FROM Rental r " +
            "WHERE r.inventory.store.storeId = :storeId " +
            "GROUP BY r.inventory.film.filmId, r.inventory.film.title " +
            "ORDER BY rentalCount DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Object[]> findTopTenRentedFilmsByStoreId(@Param("storeId") Integer storeId);
}

