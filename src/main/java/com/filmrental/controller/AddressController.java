package com.filmrental.controller;

import com.filmrental.mapper.AddressMapper;
import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<AddressDTO>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Address> addresses = addressRepository.findAll(pageable);
            return addresses.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(addresses.map(addressMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}