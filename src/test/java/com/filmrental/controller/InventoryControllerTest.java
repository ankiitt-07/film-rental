package com.filmrental.controller;

import com.filmrental.exception.ResourceNotFoundException;
import com.filmrental.mapper.InventoryMapper;
import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Inventory;
import com.filmrental.repository.InventoryRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class InventoryControllerTest {

    @InjectMocks
    private InventoryController inventoryController;

    @Mock
    private InventoryRepository inventoryRepository;

    private Inventory inventory;
    private InventoryDTO inventoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        inventoryDTO = new InventoryDTO();
        inventoryDTO.setInventoryId(1);
        inventoryDTO.setFilmId(1);
        inventoryDTO.setStoreId(1);
        inventoryDTO.setLastUpdate(LocalDateTime.now());

        inventory = InventoryMapper.toEntity(inventoryDTO);
        inventory.setInventoryId(1);
        inventory.setLastUpdate(LocalDateTime.now());
    }

    @Test
    void testAddFilmToStore_Success() {
        when(inventoryRepository.save(any(Inventory.class))).thenReturn(inventory);

        ResponseEntity<InventoryDTO> response = inventoryController.addFilmToStore(inventoryDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(inventoryDTO.getFilmId(), response.getBody().getFilmId());
    }

    @Test
    void testAddFilmToStore_InvalidFilmId() {
        inventoryDTO.setFilmId(0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            inventoryController.addFilmToStore(inventoryDTO);
        });

        assertEquals("Invalid film ID", exception.getMessage());
    }

    @Test
    void testGetAllFilmsInventory_Success() {
        when(inventoryRepository.findAll()).thenReturn(List.of(inventory));

        ResponseEntity<List<InventoryDTO>> response = inventoryController.getAllFilmsInventory();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetInventoryByStore_Success() {
        when(inventoryRepository.findByStoreStoreId(1)).thenReturn(List.of(inventory));

        ResponseEntity<List<InventoryDTO>> response = inventoryController.getInventoryByStore(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetInventoryByFilm_Success() {
        when(inventoryRepository.findByFilmFilmId(1)).thenReturn(List.of(inventory));

        ResponseEntity<List<InventoryDTO>> response = inventoryController.getInventoryByFilm(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetInventoryByFilmAndStore_Success() {
        when(inventoryRepository.findByFilmFilmIdAndStoreStoreId(1, 1)).thenReturn(List.of(inventory));

        ResponseEntity<List<InventoryDTO>> response = inventoryController.getInventoryByFilmAndStore(1, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
