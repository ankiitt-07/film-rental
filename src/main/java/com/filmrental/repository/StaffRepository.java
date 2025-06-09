package com.filmrental.repository;

import com.filmrental.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Optional<Staff> findByEmail(String email);
    List<Staff> findByStore_StoreId(Integer storeId);
    List<Staff> findByActive(Boolean active);
}