package com.filmrental.repository;


import com.filmrental.model.entity.City;
import com.filmrental.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer> {

    Optional<City> findByAddressCity(City city);}