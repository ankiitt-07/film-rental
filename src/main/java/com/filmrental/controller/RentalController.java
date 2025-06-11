package com.filmrental.controller;

import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.mapper.RentalMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Rental;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.InventoryRepository;
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
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private RentalMapper rentalMapper;

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
            return rentals.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(rentals.map(rentalMapper::toDto));
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

    @GetMapping("/toptenfilms")
    public ResponseEntity<List<Object[]>> getTopTenRentedFilms() {
        try {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Object[]> results = rentalRepository.findTopTenRentedFilms(pageable);
            List<Object[]> transformedResults = results.getContent().stream()
                    .map(row -> new Object[]{filmMapper.toDto((Film) row[0]), row[1]})
                    .collect(Collectors.toList());
            return transformedResults.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(transformedResults);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/toptenfilms/store/{id}")
    public ResponseEntity<List<Object[]>> getTopTenRentedFilmsByStore(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            storeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
            Pageable pageable = PageRequest.of(0, 10);
            Page<Object[]> results = rentalRepository.findTopTenRentedFilmsByStore(id, pageable);
            List<Object[]> transformedResults = results.getContent().stream()
                    .map(row -> new Object[]{filmMapper.toDto((Film) row[0]), row[1]})
                    .collect(Collectors.toList());
            return transformedResults.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(transformedResults);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/due/store/{id}")
    public ResponseEntity<List<CustomerDTO>> getCustomersWithDueRentalsByStore(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            storeRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + id));
            List<Customer> customers = rentalRepository.findCustomersWithDueRentalsByStore(id);
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return customerDTOs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customerDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<RentalDTO>> getRentalsByCustomer(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid customer ID");
            }
            List<Rental> rentals = rentalRepository.findByCustomer_CustomerId(id);
            return rentals.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(rentals.stream().map(rentalMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/return/{id}")
    public ResponseEntity<RentalDTO> updateReturnDate(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid rental ID");
            }
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
            rental.setReturnDate(LocalDateTime.now());
            rental.setLastUpdate(LocalDateTime.now());
            Rental updatedRental = rentalRepository.save(rental);
            return ResponseEntity.ok(rentalMapper.toDto(updatedRental));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/film")
    public ResponseEntity<FilmDTO> getFilmByRentalId(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid rental ID");
            }
            Rental rental = rentalRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found with ID: " + id));
            FilmDTO filmDTO = filmMapper.toDto(rental.getInventory().getFilm());
            return ResponseEntity.ok(filmDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}