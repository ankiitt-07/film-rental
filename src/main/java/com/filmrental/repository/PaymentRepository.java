package com.filmrental.repository;

import com.filmrental.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByCustomerId(Integer customerId);
    List<Payment> findByStaffId(Integer staffId);
    List<Payment> findByRentalId(Integer rentalId);
}