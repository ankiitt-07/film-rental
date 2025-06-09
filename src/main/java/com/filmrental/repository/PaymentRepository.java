package com.filmrental.repository;

import com.filmrental.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    @Query("SELECT p.paymentDate, SUM(p.amount) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate GROUP BY p.paymentDate")
    List<Object[]> findRevenueByDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT p.paymentDate, SUM(p.amount) FROM Payment p JOIN p.rental r JOIN r.inventory i JOIN i.store s WHERE s.storeId = :storeId AND p.paymentDate BETWEEN :startDate AND :endDate GROUP BY p.paymentDate")
    List<Object[]> findRevenueByDateAndStore(@Param("storeId") Integer storeId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT f.title, SUM(p.amount) FROM Payment p JOIN p.rental r JOIN r.inventory i JOIN i.film f GROUP BY f.filmId, f.title")
    List<Object[]> findRevenueByFilm();

    @Query("SELECT f.title, SUM(p.amount) FROM Payment p JOIN p.rental r JOIN r.inventory i JOIN i.film f WHERE f.filmId = :filmId GROUP BY f.filmId, f.title")
    List<Object[]> findRevenueByFilmId(@Param("filmId") Integer filmId);

    @Query("SELECT f.title, SUM(p.amount) FROM Payment p JOIN p.rental r JOIN r.inventory i JOIN i.film f JOIN i.store s WHERE s.storeId = :storeId GROUP BY f.filmId, f.title")
    List<Object[]> findRevenueByFilmsAndStore(@Param("storeId") Integer storeId);
}