package com.filmrental.controller;


import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.StoreRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreRepo storeRepository;
    private final StoreMapper storeMapper;


    @PostMapping("/post")
    public ResponseEntity<String> addStore(@RequestBody StoreDTO dto) {
        storeRepository.save(storeMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
    }

}

