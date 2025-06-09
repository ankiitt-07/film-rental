package com.filmrental.repository;

import com.filmrental.model.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Integer> {
    List<Rental> findByCustomer_CustomerId(Integer customerId);
    List<Rental> findByInventory_InventoryId(Integer inventoryId);
    List<Rental> findAll();
    List<Rental> findByStaff_StaffId(Integer staffId);

    //Top 10 rented films for a specific store

    List<Rental> findByInventory_Store_StoreId(Integer storeId);

    //  Find overdue rentals for a store
    List<Rental> findByInventory_Store_StoreIdAndReturnDateIsNullAndRentalDateBefore(
            Integer storeId, LocalDateTime dueDateThreshold);
}