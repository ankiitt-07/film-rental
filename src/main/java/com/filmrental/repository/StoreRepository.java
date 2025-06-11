package com.filmrental.repository;

import com.filmrental.model.entity.City;
import com.filmrental.model.entity.Country;
import com.filmrental.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findByAddress_City_City(String city);
    List<Store> findByAddress_City_Country_Country(String country);
    Optional<Store> findByAddress_Phone(String phone);
    List<Store> findByAddressCity(City city);
    List<Store> findByAddressCityCountry(Country country);
}