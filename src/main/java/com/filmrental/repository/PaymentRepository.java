package com.filmrental.repository;

import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT DATE(p.paymentDate), SUM(p.amount) FROM Payment p GROUP BY DATE(p.paymentDate) ORDER BY DATE(p.paymentDate)")
    List<Object[]> findRevenueByDateWise();

    @Query("SELECT DATE(p.paymentDate), SUM(p.amount) FROM Payment p WHERE p.rental.inventory.store.storeId = :storeId GROUP BY DATE(p.paymentDate) ORDER BY DATE(p.paymentDate)")
    List<Object[]> findRevenueByDateWiseAndStore(@Param("storeId") Integer storeId);

    @Query("SELECT p.rental.inventory.film, SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE p.rental IS NOT NULL " +
            "GROUP BY p.rental.inventory.film " +
            "ORDER BY SUM(p.amount) DESC")
    List<Object[]> findCumulativeRevenueByFilm();

    @Query("SELECT p.rental.inventory.store.storeId, SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE p.rental.inventory.film.filmId = :filmId AND p.rental IS NOT NULL " +
            "GROUP BY p.rental.inventory.store.storeId " +
            "ORDER BY SUM(p.amount) DESC")
    List<Object[]> findCumulativeRevenueByFilmAndStore(@Param("filmId") Integer filmId);

    @Query("SELECT p.rental.inventory.film, SUM(p.amount) " +
            "FROM Payment p " +
            "WHERE p.rental.inventory.store.storeId = :storeId AND p.rental IS NOT NULL " +
            "GROUP BY p.rental.inventory.film " +
            "ORDER BY SUM(p.amount) DESC")
    List<Object[]> findCumulativeRevenueByStore(@Param("storeId") Integer storeId);
}