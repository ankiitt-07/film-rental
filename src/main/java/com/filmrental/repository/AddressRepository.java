package com.filmrental.repository;

import com.filmrental.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByCity_CityId(Integer cityId);
    List<Address> findByDistrict(String district);
    List<Address> findByPostalCode(String postalCode);
}