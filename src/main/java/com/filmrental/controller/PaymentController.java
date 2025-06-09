package com.filmrental.controller;

import com.filmrental.exception.ResourceNotFoundException;
import com.filmrental.mapper.PaymentMapper;
import com.filmrental.model.dto.PaymentRequestDTO;
import com.filmrental.model.entity.Payment;
import com.filmrental.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @PutMapping("/add")
    public ResponseEntity<PaymentRequestDTO> makePayment(@RequestBody PaymentRequestDTO paymentDTO) {
        try {
            if (paymentDTO.getCustomerId() == null || paymentDTO.getStaffId() == null || paymentDTO.getRentalId() == null) {
                throw new IllegalArgumentException("Customer ID, Staff ID, and Rental ID must not be null");
            }
            if (paymentDTO.getAmount() == null || paymentDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be greater than zero");
            }
            if (paymentDTO.getPaymentDate() == null) {
                throw new IllegalArgumentException("Payment date must not be null");
            }
            Payment payment = PaymentMapper.toEntity(paymentDTO);
            payment.setLastUpdate(LocalDateTime.now());
            Payment savedPayment = paymentRepository.save(payment);
            return ResponseEntity.ok(PaymentMapper.toDto(savedPayment));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to process payment: " + e.getMessage());
        }
    }

    private List<Map<String, Object>> mapRevenueResults(List<Object[]> results, String keyName, boolean isDate) {
        try {
            return results.stream()
                    .map(result -> {
                        Map<String, Object> map = new HashMap<>();
                        if (isDate) {
                            map.put(keyName, ((LocalDateTime) result[0]).toLocalDate());
                        } else {
                            map.put(keyName, result[0]);
                        }
                        map.put("revenue", result[1]);
                        return map;
                    })
                    .toList();
        } catch (ClassCastException e) {
            throw new RuntimeException("Invalid data format returned from database: " + e.getMessage());
        }
    }

    @GetMapping("/revenue/datewise")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before or equal to end date");
            }
            List<Object[]> results = paymentRepository.findRevenueByDate(
                    startDate.atStartOfDay(),
                    endDate.atTime(23, 59, 59)
            );
            return ResponseEntity.ok(mapRevenueResults(results, "date", true));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve revenue by date: " + e.getMessage());
        }
    }

    @GetMapping("/revenue/datewise/store/{id}")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByDateAndStore(
            @PathVariable("id") Integer storeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            if (storeId == null || storeId <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            if (startDate.isAfter(endDate)) {
                throw new IllegalArgumentException("Start date must be before or equal to end date");
            }
            List<Object[]> results = paymentRepository.findRevenueByDateAndStore(
                    storeId,
                    startDate.atStartOfDay(),
                    endDate.atTime(23, 59, 59)
            );
            if (results.isEmpty()) {
                throw new ResourceNotFoundException("No revenue data found for store ID: " + storeId);
            }
            return ResponseEntity.ok(mapRevenueResults(results, "date", true));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve store revenue by date: " + e.getMessage());
        }
    }

    @GetMapping("/revenue/filmwise")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByFilm() {
        try {
            List<Object[]> results = paymentRepository.findRevenueByFilm();
            if (results.isEmpty()) {
                throw new ResourceNotFoundException("No revenue data found for films");
            }
            return ResponseEntity.ok(mapRevenueResults(results, "filmTitle", false));
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve film revenue: " + e.getMessage());
        }
    }

    @GetMapping("/revenue/film/{id}")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByFilmId(@PathVariable("id") Integer filmId) {
        try {
            if (filmId == null || filmId <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            List<Object[]> results = paymentRepository.findRevenueByFilmId(filmId);
            if (results.isEmpty()) {
                throw new ResourceNotFoundException("No revenue data found for film ID: " + filmId);
            }
            return ResponseEntity.ok(mapRevenueResults(results, "filmTitle", false));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve revenue for film ID " + filmId + ": " + e.getMessage());
        }
    }

    @GetMapping("/revenue/films/store/{id}")
    public ResponseEntity<List<Map<String, Object>>> getRevenueByFilmsAndStore(@PathVariable("id") Integer storeId) {
        try {
            if (storeId == null || storeId <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            List<Object[]> results = paymentRepository.findRevenueByFilmsAndStore(storeId);
            if (results.isEmpty()) {
                throw new ResourceNotFoundException("No revenue data found for films in store ID: " + storeId);
            }
            return ResponseEntity.ok(mapRevenueResults(results, "filmTitle", false));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve film revenue for store ID " + storeId + ": " + e.getMessage());
        }
    }
}