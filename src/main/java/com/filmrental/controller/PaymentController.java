package com.filmrental.controller;

import com.filmrental.mapper.FilmMapper;
import com.filmrental.mapper.PaymentMapper;
import com.filmrental.model.dto.PaymentDTO;
import com.filmrental.model.entity.*;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.FilmRepository;
import com.filmrental.repository.PaymentRepository;
import com.filmrental.repository.RentalRepository;
import com.filmrental.repository.StaffRepository;
import com.filmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private FilmMapper filmMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<PaymentDTO>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Payment> payments = paymentRepository.findAll(pageable);
            return payments.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(payments.map(paymentMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPayment(@RequestBody PaymentDTO paymentDTO) {
        try {
            if (paymentDTO.getCustomerId() == null || paymentDTO.getRentalId() == null || paymentDTO.getStaffId() == null || paymentDTO.getAmount() == null) {
                throw new IllegalArgumentException("Customer ID, rental ID, staff ID, and amount are required");
            }
            Customer customer = customerRepository.findById(paymentDTO.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + paymentDTO.getCustomerId()));
            Rental rental = rentalRepository.findById(paymentDTO.getRentalId())
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + paymentDTO.getRentalId()));
            Staff staff = staffRepository.findById(paymentDTO.getStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + paymentDTO.getStaffId()));
            Payment payment = paymentMapper.toEntity(paymentDTO);
            payment.setCustomer(customer);
            payment.setRental(rental);
            payment.setStaff(staff);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setLastUpdate(LocalDateTime.now());
            paymentRepository.save(payment);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating payment");
        }
    }

    @GetMapping("/revenue/datewise")
    public ResponseEntity<List<Object[]>> getRevenueByDateWise() {
        try {
            List<Object[]> results = paymentRepository.findRevenueByDateWise();
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/revenue/datewise/store/{id}")
    public ResponseEntity<List<Object[]>> getRevenueByDateWiseAndStore(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            List<Object[]> results = paymentRepository.findRevenueByDateWiseAndStore(id);
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/revenue/filmwise")
    public ResponseEntity<List<Object[]>> getCumulativeRevenueByFilm() {
        try {
            List<Object[]> results = paymentRepository.findCumulativeRevenueByFilm();
            List<Object[]> transformedResults = results.stream()
                    .map(row -> new Object[]{filmMapper.toDto((Film) row[0]), row[1]})
                    .collect(Collectors.toList());
            return transformedResults.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(transformedResults);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/revenue/film/{id}")
    public ResponseEntity<List<Object[]>> getCumulativeRevenueByFilmAndStore(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            List<Object[]> results = paymentRepository.findCumulativeRevenueByFilmAndStore(id);
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/revenue/film/store/{id}")
    public ResponseEntity<List<Object[]>> getCumulativeRevenueByStore(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            storeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
            List<Object[]> results = paymentRepository.findCumulativeRevenueByStore(id);
            List<Object[]> transformedResults = results.stream()
                    .map(row -> new Object[]{filmMapper.toDto((Film) row[0]), row[1]})
                    .collect(Collectors.toList());
            return transformedResults.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(transformedResults);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}