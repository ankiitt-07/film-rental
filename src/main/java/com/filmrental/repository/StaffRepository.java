package com.filmrental.repository;

import com.filmrental.model.entity.City;
import com.filmrental.model.entity.Country;
import com.filmrental.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByLastNameContainingIgnoreCase(String lastName);
    List<Staff> findByFirstNameContainingIgnoreCase(String firstName);
    Optional<Staff> findByEmail(String email);
    List<Staff> findByAddress_City_City(String city);
    List<Staff> findByAddress_City_Country_Country(String country);
    Optional<Staff> findByAddress_Phone(String phone);
    List<Staff> findByActiveTrue();
    List<Staff> findByActiveFalse();
    List<Staff> findByStore_StoreId(Integer storeId);
    List<Staff> findByStore_StoreIdAndActive(Integer storeId, boolean active);
    List<Staff> findByAddressCityCountry(Country country);
    List<Staff> findByAddressCity(City city);
}