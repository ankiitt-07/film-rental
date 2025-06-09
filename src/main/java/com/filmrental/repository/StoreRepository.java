package com.filmrental.repository;

import com.filmrental.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Integer> {
    List<Store> findByManagerStaff_StaffId(Integer staffId);
    List<Store> findByAddress_AddressId(Short addressId);
}