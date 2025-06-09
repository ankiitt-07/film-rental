package com.filmrental.controller;

import com.filmrental.model.dto.FilmRentalCountDTO;
import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Rental;
import com.filmrental.model.entity.Staff;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.InventoryRepository;
import com.filmrental.repository.RentalRepository;
import com.filmrental.mapper.RentalMapper;
import com.filmrental.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private InventoryRepository InventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<RentalDTO>> getRentalsByCustomerId(@PathVariable("id") Integer customerId) {
        try {
            List<Rental> rentals = rentalRepository.findByCustomer_CustomerId(customerId);
            if (rentals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<RentalDTO> rentalDTOs = rentals.stream()
                    .map(RentalMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(rentalDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching rentals for customer ID: " + customerId);
        }
    }

    @GetMapping("/toptenfilms")
    public ResponseEntity<List<FilmRentalCountDTO>> getTopTenRentedFilms() {
        try {
            List<Rental> rentals = rentalRepository.findAll();
            if (rentals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<FilmRentalCountDTO> topFilms = rentals.stream()
                    .collect(Collectors.groupingBy(
                            rental -> rental.getInventory().getFilm().getFilmId(),
                            Collectors.counting()
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        Rental firstRental = rentals.stream()
                                .filter(r -> r.getInventory().getFilm().getFilmId().equals(entry.getKey()))
                                .findFirst()
                                .orElse(null);
                        return new FilmRentalCountDTO(
                                entry.getKey(),
                                firstRental != null ? firstRental.getInventory().getFilm().getTitle() : "Unknown",
                                entry.getValue()
                        );
                    })
                    .sorted((a, b) -> b.rentalCount().compareTo(a.rentalCount()))
                    .limit(10)
                    .collect(Collectors.toList());
            if (topFilms.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(topFilms);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching top 10 rented films");
        }
    }

    @GetMapping("/toptenfilms/store/{id}")
    public ResponseEntity<List<FilmRentalCountDTO>> getTopTenRentedFilmsByStore(@PathVariable("id") Integer storeId) {
        try {
            List<Rental> rentals = rentalRepository.findByInventory_Store_StoreId(storeId);
            if (rentals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<FilmRentalCountDTO> topFilms = rentals.stream()
                    .collect(Collectors.groupingBy(
                            rental -> rental.getInventory().getFilm().getFilmId(),
                            Collectors.counting()
                    ))
                    .entrySet().stream()
                    .map(entry -> {
                        Rental firstRental = rentals.stream()
                                .filter(r -> r.getInventory().getFilm().getFilmId().equals(entry.getKey()))
                                .findFirst()
                                .orElse(null);
                        return new FilmRentalCountDTO(
                                entry.getKey(),
                                firstRental != null ? firstRental.getInventory().getFilm().getTitle() : "Unknown",
                                entry.getValue()
                        );
                    })
                    .sorted((a, b) -> b.rentalCount().compareTo(a.rentalCount()))
                    .limit(10)
                    .collect(Collectors.toList());
            if (topFilms.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(topFilms);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching top 10 rented films for store ID: " + storeId);
        }
    }

    @GetMapping("/due/store/{id}")
    public ResponseEntity<List<RentalDTO>> getOverdueRentalsByStore(@PathVariable("id") Integer storeId) {
        try {
            LocalDateTime dueDateThreshold = LocalDateTime.now().minusDays(7); // Assuming 7 days rental period
            List<Rental> overdueRentals = rentalRepository.findByInventory_Store_StoreIdAndReturnDateIsNullAndRentalDateBefore(
                    storeId, dueDateThreshold);
            if (overdueRentals.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<RentalDTO> overdueRentalDTOs = overdueRentals.stream()
                    .map(RentalMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(overdueRentalDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching overdue rentals for store ID: " + storeId);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addRental(@RequestBody RentalDTO rentalDTO) {
        try {
            if (rentalDTO == null || rentalDTO.inventoryId() == null || rentalDTO.customerId() == null || rentalDTO.staffId() == null) {
                throw new IllegalArgumentException("Invalid rental data: inventoryId, customerId, and staffId are required");
            }

            // Validate related entities
            Inventory inventory = InventoryRepository.findById(rentalDTO.inventoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Inventory not found for ID: " + rentalDTO.inventoryId()));
            Customer customer = customerRepository.findById(rentalDTO.customerId())
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + rentalDTO.customerId()));
            Staff staff = staffRepository.findById(rentalDTO.staffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + rentalDTO.staffId()));

            // Create and save rental
            Rental rental = RentalMapper.toEntity(rentalDTO);
            rental.setInventory(inventory);
            rental.setCustomer(customer);
            rental.setStaff(staff);
            rental.setRentalDate(LocalDateTime.now());
            rental.setLastUpdate(LocalDateTime.now());
            rentalRepository.save(rental);

            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating rental: " + e.getMessage());
        }
    }

    @PostMapping("/update/returndate/{id}")
    public ResponseEntity<RentalDTO> updateReturnDate(@PathVariable("id") Integer rentalId) {
        try {
            Rental rental = rentalRepository.findById(rentalId)
                    .orElseThrow(() -> new IllegalArgumentException("Rental not found for ID: " + rentalId));
            if (rental.getReturnDate() != null) {
                throw new IllegalArgumentException("Rental ID: " + rentalId + " has already been returned");
            }
            rental.setReturnDate(LocalDateTime.now());
            rental.setLastUpdate(LocalDateTime.now());
            Rental updatedRental = rentalRepository.save(rental);
            return ResponseEntity.ok(RentalMapper.toDto(updatedRental));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating return date for rental ID: " + rentalId);
        }
    }
}