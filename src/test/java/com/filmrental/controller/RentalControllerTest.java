package com.filmrental.controller;

import com.filmrental.mapper.RentalMapper;
import com.filmrental.model.dto.RentalDTO;
import com.filmrental.model.dto.FilmRentalCountDTO;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Rental;
import com.filmrental.model.entity.Staff;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.InventoryRepository;
import com.filmrental.repository.RentalRepository;
import com.filmrental.repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RentalControllerTest {

    @InjectMocks
    private RentalController rentalController;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StaffRepository staffRepository;

    private Rental rental;
    private RentalDTO rentalDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        rentalDTO = new RentalDTO(
                1,                         // rentalId
                LocalDateTime.now(),       // rentalDate
                null,                      // returnDate (not yet returned)
                LocalDateTime.now(),       // lastUpdate
                1,                         // inventoryId
                1,                         // customerId
                1                          // staffId
        );

        rental = RentalMapper.toEntity(rentalDTO);
        rental.setInventory(new Inventory());
        rental.setCustomer(new Customer());
        rental.setStaff(new Staff());
    }

    @Test
    void testGetRentalsByCustomerId_Success() {
        when(rentalRepository.findByCustomer_CustomerId(1)).thenReturn(List.of(rental));

        ResponseEntity<List<RentalDTO>> response = rentalController.getRentalsByCustomerId(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetRentalsByCustomerId_NotFound() {
        when(rentalRepository.findByCustomer_CustomerId(1)).thenReturn(List.of());

        ResponseEntity<List<RentalDTO>> response = rentalController.getRentalsByCustomerId(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testAddRental_Success() {
        when(inventoryRepository.findById(1)).thenReturn(Optional.of(new Inventory()));
        when(customerRepository.findById(1)).thenReturn(Optional.of(new Customer()));
        when(staffRepository.findById(1)).thenReturn(Optional.of(new Staff()));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        ResponseEntity<String> response = rentalController.addRental(rentalDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Record Created Successfully", response.getBody());
    }

    @Test
    void testAddRental_InvalidData() {
        RentalDTO invalidDTO = new RentalDTO(null, LocalDateTime.now(), null, LocalDateTime.now(), null, 1, 1);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rentalController.addRental(invalidDTO);
        });

        assertEquals("Error creating rental: Invalid rental data: inventoryId, customerId, and staffId are required", exception.getMessage());
        verify(rentalRepository, never()).save(any(Rental.class));
    }

    @Test
    void testUpdateReturnDate_Success() {
        rental.setReturnDate(null);
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));
        when(rentalRepository.save(any(Rental.class))).thenReturn(rental);

        ResponseEntity<RentalDTO> response = rentalController.updateReturnDate(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().returnDate());
    }

    @Test
    void testUpdateReturnDate_AlreadyReturned() {
        // Simulate an already returned rental
        rental.setReturnDate(LocalDateTime.now());

        // Mock repository behavior
        when(rentalRepository.findById(1)).thenReturn(Optional.of(rental));

        // Expect IllegalArgumentException to be thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rentalController.updateReturnDate(1);
        });

        // Ensure message exactly matches the one in RentalController
        assertEquals("Error updating return date for rental ID: 1", exception.getMessage());
        // Verify repository interaction
        verify(rentalRepository, times(1)).findById(1);
        verify(rentalRepository, never()).save(any(Rental.class)); // Ensure save() was **never** called
    }


}
