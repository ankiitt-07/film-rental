package com.filmrental.repository;

import com.filmrental.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    List<Staff> findByLastName(String lastName);
    List<Staff> findByFirstName(String firstName);
    Staff findByEmail(String email);
    List<Staff> findByAddress_City_City(String city);
    List<Staff> findByAddress_City_Country_Country(String country);
    List<Staff> findByPhone(String phone);
}