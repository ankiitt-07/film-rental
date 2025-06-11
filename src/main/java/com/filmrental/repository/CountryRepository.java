package com.filmrental.repository;

import com.filmrental.model.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CountryRepository extends JpaRepository<Country, Integer> {
    Optional<Country> findByCountry(String country);
    Optional<Country> findByCountryIgnoreCase(String country);

}