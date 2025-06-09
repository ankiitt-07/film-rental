package com.filmrental.repository;

import com.filmrental.model.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByCustomer_CustomerId(Integer customerId);
    List<Rental> findByInventory_InventoryId(Integer inventoryId);
    List<Rental> findByStaff_StaffId(Integer staffId);
}