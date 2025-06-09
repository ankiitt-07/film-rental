package com.filmrental.repository;

import com.filmrental.model.entity.City;
import com.filmrental.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByFirstNameContainingIgnoreCase(String first_name);
    List<Staff> findByLastNameContainingIgnoreCase(String last_name);
    Staff findByEmail(String email);
    Staff findByPhone(String phone);
    @Query("SELECT s FROM Staff s WHERE s.address.city.city = :city")
    List<Staff> findByCity(String city);
    @Query("SELECT s FROM Staff s WHERE s.address.city.country.country = :country")
    List<Staff> findByCountry(String country);
    Optional<City> findByAddressCity(City city);}