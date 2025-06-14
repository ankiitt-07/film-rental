package com.filmrental.controller;

import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.mapper.RentalMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.entity.*;
import com.filmrental.repository.*;
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
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<RentalDTO>> getAllRentals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Rental> rentals = rentalRepository.findAll(pageable);
            Page<RentalDTO> rentalDTOs = rentals.map(rentalMapper::toDto);
            return ResponseEntity.ok(rentalDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalDTO> getRentalById(@PathVariable Integer id) {
        try {
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
            return ResponseEntity.ok(rentalMapper.toDto(rental));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRental(@RequestBody RentalDTO rentalDTO) {
        try {
            if (rentalDTO.getCustomerId() == null || rentalDTO.getInventoryId() == null || rentalDTO.getStaffId() == null) {
                throw new IllegalArgumentException("Customer ID, inventory ID, and staff ID are required");
            }
            Customer customer = customerRepository.findById(rentalDTO.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + rentalDTO.getCustomerId()));
            Inventory inventory = inventoryRepository.findById(rentalDTO.getInventoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found with ID: " + rentalDTO.getInventoryId()));
            Staff staff = staffRepository.findById(rentalDTO.getStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + rentalDTO.getStaffId()));
            Rental rental = rentalMapper.toEntity(rentalDTO);
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rental.setRentalDate(LocalDateTime.now());
            rental.setLastUpdate(LocalDateTime.now());
            rentalRepository.save(rental);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating rental");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateRental(@PathVariable Integer id, @RequestBody RentalDTO rentalDTO) {
        try {
            if (id <= 0 || rentalDTO.getCustomerId() == null || rentalDTO.getInventoryId() == null || rentalDTO.getStaffId() == null) {
                throw new IllegalArgumentException("Invalid rental ID or missing required fields");
            }
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
            Customer customer = customerRepository.findById(rentalDTO.getCustomerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + rentalDTO.getCustomerId()));
            Inventory inventory = inventoryRepository.findById(rentalDTO.getInventoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found with ID: " + rentalDTO.getInventoryId()));
            Staff staff = staffRepository.findById(rentalDTO.getStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + rentalDTO.getStaffId()));
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rental.setRentalDate(rentalDTO.getRentalDate() != null ? rentalDTO.getRentalDate() : rental.getRentalDate());
            rental.setReturnDate(rentalDTO.getReturnDate());
            rental.setLastUpdate(LocalDateTime.now());
            rentalRepository.save(rental);
            return ResponseEntity.ok("Rental updated successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating rental");
        }
    }

    @PutMapping("/update/return/{id}")
    public ResponseEntity<RentalDTO> updateReturnDate(@PathVariable Integer id) {
        try {
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
            rental.setReturnDate(LocalDateTime.now());
            rental.setLastUpdate(LocalDateTime.now());
            rentalRepository.save(rental);
            return ResponseEntity.ok(rentalMapper.toDto(rental));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/toptenfilms")
    public ResponseEntity<List<Object[]>> getTopTenRentedFilms() {
        try {
            List<Object[]> topFilms = rentalRepository.findTopTenRentedFilms();
            return ResponseEntity.ok(topFilms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/toptenfilms/store/{id}")
    public ResponseEntity<List<Object[]>> getTopTenRentedFilmsByStore(@PathVariable Integer id) {
        try {
            Store store = storeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
            List<Object[]> topFilms = rentalRepository.findTopTenRentedFilmsByStore(id);
            return ResponseEntity.ok(topFilms);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/due/store/{id}")
    public ResponseEntity<List<CustomerDTO>> getCustomersWithDueRentalsByStore(@PathVariable Integer id) {
        try {
            Store store = storeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
            List<Customer> customers = rentalRepository.findCustomersWithDueRentalsByStore(id);
            List<CustomerDTO> customerDTOs = customers.stream().map(customerMapper::toDto).collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<RentalDTO>> getRentalsByCustomer(@PathVariable Integer id) {
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
            List<Rental> rentals = rentalRepository.findByCustomer_CustomerId(id);
            List<RentalDTO> rentalDTOs = rentals.stream().map(rentalMapper::toDto).collect(Collectors.toList());
            return ResponseEntity.ok(rentalDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/film")
    public ResponseEntity<FilmDTO> getFilmByRentalId(@PathVariable Integer id) {
        try {
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
            Film film = rental.getInventory().getFilm();
            return ResponseEntity.ok(filmMapper.toDto(film));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}