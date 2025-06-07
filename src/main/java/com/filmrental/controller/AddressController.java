package com.filmrental.controller;

import com.filmrental.exception.ResourceNotFoundException;
import com.filmrental.mapper.AddressMapper;
import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressRepository addressRepository;
    private final CityRepository cityRepository;
    private final AddressMapper addressMapper;

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Short id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + id));
        return ResponseEntity.ok(addressMapper.toAddressDTO(address));
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO) {
        City city = cityRepository.findById(addressDTO.getCityId())
                .orElseThrow(() -> new ResourceNotFoundException("City not found with id: " + addressDTO.getCityId()));
        Address address = addressMapper.toAddressEntity(addressDTO);
        address.setCity(city);
        address = addressRepository.save(address);
        return ResponseEntity.ok(addressMapper.toAddressDTO(address));
    }
}