package com.filmrental.repository;

import com.filmrental.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByPhone(String phone);
    List<Address> findByCity_City(String city);
    List<Address> findByCity_Country_Country(String country);
}