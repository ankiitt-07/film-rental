package com.filmrental.repository;

import com.filmrental.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> findByLastNameContainingIgnoreCase(String lastName);
    List<Customer> findByFirstNameContainingIgnoreCase(String firstName);
    Optional<Customer> findByEmail(String email);
    List<Customer> findByActiveTrue();
    List<Customer> findByActiveFalse();
    Optional<Customer> findByAddress_Phone(String phone);
    List<Customer> findByAddress_City_City(String city);
    List<Customer> findByAddress_City_Country_Country(String country);
}