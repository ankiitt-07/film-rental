package com.filmrental.controller;

import com.filmrental.exception.ResourceNotFoundException;
import com.filmrental.mapper.InventoryMapper;
import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Inventory;
import com.filmrental.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping("/add")
    public ResponseEntity<InventoryDTO> addFilmToStore(@RequestBody InventoryDTO inventoryDTO) {
        try {
            if (inventoryDTO.getFilmId() == null || inventoryDTO.getFilmId() <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            if (inventoryDTO.getStoreId() == null || inventoryDTO.getStoreId() <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            Inventory inventory = InventoryMapper.toEntity(inventoryDTO);
            inventory.setLastUpdate(LocalDateTime.now());
            Inventory savedInventory = inventoryRepository.save(inventory);
            return ResponseEntity.ok(InventoryMapper.toDto(savedInventory));
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add inventory: " + e.getMessage());
        }
    }

    @GetMapping("/films")
    public ResponseEntity<List<InventoryDTO>> getAllFilmsInventory() {
        try {
            List<Inventory> inventories = inventoryRepository.findAll();
            if (inventories.isEmpty()) {
                throw new ResourceNotFoundException("No inventory records found");
            }
            List<InventoryDTO> inventoryDTOs = inventories.stream()
                    .map(InventoryMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(inventoryDTOs);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve inventory of all films: " + e.getMessage());
        }
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByStore(@PathVariable("id") Integer storeId) {
        try {
            if (storeId == null || storeId <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            List<Inventory> inventories = inventoryRepository.findByStoreStoreId(storeId);
            if (inventories.isEmpty()) {
                throw new ResourceNotFoundException("No inventory found for store ID: " + storeId);
            }
            List<InventoryDTO> inventoryDTOs = inventories.stream()
                    .map(InventoryMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(inventoryDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve inventory for store ID " + storeId + ": " + e.getMessage());
        }
    }

    @GetMapping("/film/{id}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByFilm(@PathVariable("id") Integer filmId) {
        try {
            if (filmId == null || filmId <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            List<Inventory> inventories = inventoryRepository.findByFilmFilmId(filmId);
            if (inventories.isEmpty()) {
                throw new ResourceNotFoundException("No inventory found for film ID: " + filmId);
            }
            List<InventoryDTO> inventoryDTOs = inventories.stream()
                    .map(InventoryMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(inventoryDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve inventory for film ID " + filmId + ": " + e.getMessage());
        }
    }

    @GetMapping("/film/{filmId}/store/{storeId}")
    public ResponseEntity<List<InventoryDTO>> getInventoryByFilmAndStore(
            @PathVariable("filmId") Integer filmId,
            @PathVariable("storeId") Integer storeId) {
        try {
            if (filmId == null || filmId <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            if (storeId == null || storeId <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            List<Inventory> inventories = inventoryRepository.findByFilmFilmIdAndStoreStoreId(filmId, storeId);
            if (inventories.isEmpty()) {
                throw new ResourceNotFoundException("No inventory found for film ID " + filmId + " in store ID " + storeId);
            }
            List<InventoryDTO> inventoryDTOs = inventories.stream()
                    .map(InventoryMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(inventoryDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve inventory for film ID " + filmId + " and store ID " + storeId + ": " + e.getMessage());
        }
    }
}