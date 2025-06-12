package com.filmrental.controller;

import com.filmrental.mapper.InventoryMapper;
import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Inventory;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.FilmRepository;
import com.filmrental.repository.InventoryRepository;
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
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private InventoryMapper inventoryMapper;

    //fetching all the inventory records from the inventory table in our database
    @GetMapping("/all")
    public ResponseEntity<Page<InventoryDTO>> getAllInventory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Inventory> inventory = inventoryRepository.findAll(pageable);
            return inventory.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(inventory.map(inventoryMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //adds a new record in DB
    @PostMapping("/add")
    public ResponseEntity<String> addInventory(@RequestBody InventoryDTO inventoryDTO) {
        try {
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
            inventoryRepository.save(inventory);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding inventory");
        }
    }

    //fetches film name and total count of each
    @GetMapping("/films")
    public ResponseEntity<List<Object[]>> getInventoryCountByFilm(){
        try {
            List<Object[]> results = inventoryRepository.findInventoryCountByFilm();
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //fetches count for each film in specific store
    @GetMapping("/store/{id}")
    public ResponseEntity<List<Object[]>> getInventoryByStore(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            List<Object[]> results = inventoryRepository.findInventoryCountByStore(id);
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //displays the count of specific film in all stores
    @GetMapping("/film/{id}")
    public ResponseEntity<List<Object[]>> getInventoryByFilm(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            List<Object[]> results = inventoryRepository.findInventoryCountByFilmId(id);
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    //display count of specific film at specific store
    @GetMapping("/film/{filmId}/store/{storeId}")
    public ResponseEntity<Object[]> getInventoryByFilmAndStore(@PathVariable Integer filmId, @PathVariable Integer storeId) {
        try {
            if (filmId <= 0 || storeId <= 0) {
                throw new IllegalArgumentException("Invalid film or store ID");
            }
            Object[] result = inventoryRepository.findInventoryCountByFilmIdAndStore(filmId, storeId);
            return result == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}