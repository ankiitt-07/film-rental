package com.filmrental.controller;


import com.filmrental.model.dto.FilmRentalCountDTO;

import com.filmrental.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rental")
public class RentalController {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;


    // GET /api/rental/toptenfilms — Top 10 Rented Films
    @GetMapping("/toptenfilms")
    public ResponseEntity<List<FilmRentalCountDTO>> getTopTenRentedFilms() {
        List<Object[]> results = rentalRepository.findTopTenRentedFilms();
        List<FilmRentalCountDTO> topFilms = results.stream()
                .map(result -> new FilmRentalCountDTO(
                        (Integer) result[0], // filmId
                        (String) result[1],  // title
                        (Long) result[2]     // rentalCount
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(topFilms);
    }

    // GET /api/rental/toptenfilms/store/{id} — Top 10 Rented Films of Store
    @GetMapping("/toptenfilms/store/{id}")
    public ResponseEntity<List<FilmRentalCountDTO>> getTopTenRentedFilmsByStore(@PathVariable Integer id) {
        List<Object[]> results = rentalRepository.findTopTenRentedFilmsByStoreId(id);
        List<FilmRentalCountDTO> topFilms = results.stream()
                .map(result -> new FilmRentalCountDTO(
                        (Integer) result[0], // filmId
                        (String) result[1],  // title
                        (Long) result[2]     // rentalCount
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(topFilms);
    }


}

