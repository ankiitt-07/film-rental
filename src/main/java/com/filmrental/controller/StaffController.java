package com.filmrental.controller;

import com.filmrental.mapper.AddressMapper;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.StaffRepository;
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
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private AddressMapper addressMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<StaffDTO>> getAllStaff(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Staff> staff = staffRepository.findAll(pageable);
            return staff.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(staff.map(staffMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addStaff(@RequestBody StaffDTO staffDTO) {
        try {
            if (staffDTO.getFirstName() == null || staffDTO.getLastName() == null || staffDTO.getAddressId() == null || staffDTO.getEmail() == null) {
                throw new IllegalArgumentException("First name, last name, address ID, and email are required");
            }

            if (staffRepository.findByEmail(staffDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }

            Address address = addressRepository.findById(staffDTO.getAddressId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + staffDTO.getAddressId()));

            Store store = storeRepository.findById(staffDTO.getStoreId())
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + staffDTO.getStoreId()));

            Staff staff = staffMapper.toEntity(staffDTO);
            staff.setAddress(address);
            staff.setStore(store);
            staff.setLastUpdate(LocalDateTime.now());

            staffRepository.save(staff);

            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace(); // log the exact issue during development
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating staff");
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StaffDTO> getStaffByEmail(@PathVariable String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            Staff staff = staffRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with email: " + email));
            return ResponseEntity.ok(staffMapper.toDto(staff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<StaffDTO>> getStaffByCountry(@PathVariable String country) {
        try {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("Country cannot be empty");
            }
            List<Staff> staff = staffRepository.findByAddressCityCountryCountryContainingIgnoreCase(country);
            return staff.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(staff.stream().map(staffMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<StaffDTO> getStaffByPhone(@PathVariable String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be empty");
            }
            Staff staff = staffRepository.findByAddressPhone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with phone number: " + phone));
            return ResponseEntity.ok(staffMapper.toDto(staff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<StaffDTO>> getStaffByLastName(@PathVariable String ln) {
        try {
            if (ln == null || ln.trim().isEmpty()) {
                throw new IllegalArgumentException("Last name cannot be empty");
            }
            List<Staff> staff = staffRepository.findByLastNameContainingIgnoreCase(ln);
            return staff.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(staff.stream().map(staffMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/fn/{id}")
    public ResponseEntity<StaffDTO> updateFirstName(@PathVariable Integer id, @RequestBody String firstName) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staff ID");
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("First name cannot be empty");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
            staff.setFirstName(firstName);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.toDto(updatedStaff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/ln/{id}")
    public ResponseEntity<StaffDTO> updateLastName(@PathVariable Integer id, @RequestBody String lastName) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staff ID");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Last name cannot be empty");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
            staff.setLastName(lastName);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.toDto(updatedStaff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/email/{id}")
    public ResponseEntity<StaffDTO> updateEmail(@PathVariable Integer id, @RequestBody String email) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staff ID");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            if (staffRepository.findByEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
            staff.setEmail(email);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.toDto(updatedStaff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/store/{id}")
    public ResponseEntity<StaffDTO> updateStore(@PathVariable Integer id, @RequestBody Integer storeId) {
        try {
            if (id <= 0 || storeId <= 0) {
                throw new IllegalArgumentException("Invalid staff or store ID");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
            staff.setStore(store);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.toDto(updatedStaff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}/address")
    public ResponseEntity<StaffDTO> updateAddress(@PathVariable Integer id, @RequestBody Integer addressId) {
        try {
            if (id <= 0 || addressId <= 0) {
                throw new IllegalArgumentException("Invalid staff or address ID");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));
            staff.setAddress(address);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.toDto(updatedStaff));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/address")
    public ResponseEntity<AddressDTO> getAddressByStaffId(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staff ID");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + id));
            AddressDTO addressDTO = addressMapper.toDto(staff.getAddress());
            return ResponseEntity.ok(addressDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}