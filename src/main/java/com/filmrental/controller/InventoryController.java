package com.filmrental.controller;

import com.filmrental.mapper.InventoryMapper;
import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.FilmRepository;
import com.filmrental.repository.InventoryRepository;
import com.filmrental.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private static final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<InventoryDTO>> getAllInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Inventory> inventory = inventoryRepository.findAll(pageable);
            return inventory.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(inventory.map(inventoryMapper::toDto));
        } catch (Exception e) {
            logger.error("Error fetching inventories: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> addInventory(@RequestBody InventoryDTO inventoryDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            logger.info("Received InventoryDTO: {}", inventoryDTO);
            if (inventoryDTO.getFilmId() == null || inventoryDTO.getStoreId() == null) {
                throw new IllegalArgumentException("Film ID and store ID are required");
            }
            Film film = filmRepository.findById(inventoryDTO.getFilmId())
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + inventoryDTO.getFilmId()));
            Store store = storeRepository.findById(inventoryDTO.getStoreId())
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + inventoryDTO.getStoreId()));
            Inventory inventory = inventoryMapper.toEntity(inventoryDTO);
            inventory.setFilm(film);
            inventory.setStore(store);
            inventory.setLastUpdate(LocalDateTime.now());
            logger.info("Saving Inventory: {}", inventory);
            inventoryRepository.save(inventory);
            response.put("message", "Record Created Successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error: {}", e.getMessage());
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            logger.error("Error adding inventory: {}", e.getMessage(), e);
            response.put("error", "Internal Server Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/films")
    public ResponseEntity<List<Object[]>> getInventoryCountByFilm() {
        try {
            List<Object[]> results = inventoryRepository.findInventoryCountByFilm();
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (Exception e) {
            logger.error("Error fetching inventory count by film: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/store/{id}")
    public ResponseEntity<List<Object[]>> getInventoryByStore(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            List<Object[]> results = inventoryRepository.findInventoryCountByStore(id);
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Error fetching inventory by store: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/film/{id}")
    public ResponseEntity<List<Object[]>> getInventoryByFilm(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            List<Object[]> results = inventoryRepository.findInventoryCountByFilmId(id);
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Error fetching inventory by film: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/film/{filmId}/store/{storeId}")
    public ResponseEntity<Object[]> getInventoryByFilmAndStore(@PathVariable Integer filmId, @PathVariable Integer storeId) {
        try {
            if (filmId <= 0 || storeId <= 0) {
                throw new IllegalArgumentException("Invalid film or store ID");
            }
            Object[] result = inventoryRepository.findInventoryCountByFilmIdAndStore(filmId, storeId);
            return result == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error("Error fetching inventory by film and store: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
