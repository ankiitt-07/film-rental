package com.filmrental.repository;

import com.filmrental.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    @Query("SELECT s FROM Store s WHERE LOWER(s.address.city.city) LIKE LOWER(CONCAT('%', :city, '%'))")
    List<Store> findByAddressCityCityContainingIgnoreCase(@Param("city") String city);

    @Query("SELECT s FROM Store s WHERE LOWER(s.address.city.country.country) LIKE LOWER(CONCAT('%', :country, '%'))")
    List<Store> findByAddressCityCountryCountryContainingIgnoreCase(@Param("country") String country);

    @Query("SELECT s FROM Store s WHERE s.address.phone = :phone")
    Optional<Store> findByAddressPhone(@Param("phone") String phone);
}