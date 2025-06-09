package com.filmrental.repository;

import com.filmrental.model.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByCity(String city);
    List<City> findByCountry_CountryId(Integer countryId);
}