package com.filmrental.controller;

import com.filmrental.model.dto.InventoryDTO;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.FilmRepository;
import com.filmrental.repository.InventoryRepository;
import com.filmrental.repository.StoreRepository;
import com.filmrental.mapper.InventoryMapper;
import com.filmrental.model.entity.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryRepository inventoryRepository;
    private final FilmRepository filmRepository;
    private final StoreRepository storeRepository;
    private final InventoryMapper inventoryMapper;

    // 1. Add Inventory
    @PostMapping("/add")
    public ResponseEntity<String> addInventory(@RequestBody InventoryDTO dto) {
        Optional<Film> filmOpt = filmRepository.findById(dto.getFilmId());
        Optional<Store> storeOpt = storeRepository.findById(dto.getStoreId());

        if (filmOpt.isEmpty() || storeOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid film or store ID");
        }

        Inventory inventory = inventoryMapper.toEntity(dto, filmOpt.get(), storeOpt.get());
        inventoryRepository.save(inventory);
        return ResponseEntity.ok("Record Created successfully");
    }

    // 2. All Films Inventory
    @GetMapping("/films")
    public ResponseEntity<List<Map<String, Object>>> getAllFilmInventory() {
        List<Object[]> results = inventoryRepository.countAllFilms();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", row[0]);
            map.put("count", row[1]);
            response.add(map);
        }

        return ResponseEntity.ok(response);
    }

    // 3. Inventory of a Store
    @GetMapping("/store/{id}")
    public ResponseEntity<List<Map<String, Object>>> getStoreInventory(@PathVariable Long id) {
        List<Inventory> inventoryList = inventoryRepository.findByStoreStoreId(id);
        Map<String, Long> filmCountMap = new HashMap<>();

        for (Inventory inv : inventoryList) {
            String title = inv.getFilm().getTitle();
            filmCountMap.put(title, filmCountMap.getOrDefault(title, 0L) + 1);
        }

        List<Map<String, Object>> response = filmCountMap.entrySet().stream().map(entry -> {
            Map<String, Object> m = new HashMap<>();
            m.put("title", entry.getKey());
            m.put("count", entry.getValue());
            return m;
        }).toList();

        return ResponseEntity.ok(response);
    }

    // 4. Inventory of a Film across Stores
    @GetMapping("/film/{id}")
    public ResponseEntity<Map<String, Long>> getInventoryByFilm(@PathVariable Long id) {
        List<Inventory> inventoryList = inventoryRepository.findByFilmFilmId(id);
        Map<String, Long> storeCountMap = new HashMap<>();

        for (Inventory inv : inventoryList) {
            String storeName = "Store " + inv.getStore().getStoreId(); // Change if Store has name field
            storeCountMap.put(storeName, storeCountMap.getOrDefault(storeName, 0L) + 1);
        }

        return ResponseEntity.ok(storeCountMap);
    }

    // 5. Inventory of a Film in a Store
    @GetMapping("/film/{filmId}/store/{storeId}")
    public ResponseEntity<Map<String, Object>> getInventoryByFilmInStore(
            @PathVariable Long filmId,
            @PathVariable Long storeId
    ) {
        Long count = inventoryRepository.countByFilmFilmIdAndStoreStoreId(filmId, storeId);
        Map<String, Object> result = new HashMap<>();
        result.put("filmId", filmId);
        result.put("storeId", storeId);
        result.put("count", count);

        return ResponseEntity.ok(result);
    }
}
