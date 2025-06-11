package com.filmrental.repository;

import com.filmrental.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByCity(String city);
}