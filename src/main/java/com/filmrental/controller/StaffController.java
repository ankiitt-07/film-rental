package com.filmrental.controller;

import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.StaffRepository;
import com.filmrental.repository.StoreRepository;
import com.filmrental.mapper.StaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/post")
    public ResponseEntity<String> createStaff(@RequestBody StaffDTO staffDTO) {
        try {
            if (staffDTO == null || staffDTO.getFirstName() == null || staffDTO.getLastName() == null || staffDTO.getEmail() == null) {
                throw new IllegalArgumentException("Invalid staff data: firstName, lastName, and email are required");
            }
            if (staffDTO.getAddressId() == null || staffDTO.getStoreId() == null) {
                throw new IllegalArgumentException("Invalid staff data: addressId and storeId are required");
            }
            if (staffDTO.getUsername() == null) {
                throw new IllegalArgumentException("Invalid staff data: username is required");
            }
            if (staffDTO.getActive() == null) {
                throw new IllegalArgumentException("Invalid staff data: active status is required");
            }
            Address address = addressRepository.findById(staffDTO.getAddressId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found for ID: " + staffDTO.getAddressId()));
            Store store = storeRepository.findById(staffDTO.getStoreId())
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + staffDTO.getStoreId()));
            Staff staff = staffMapper.DTOToStaff(staffDTO);
            staff.setAddress(address);
            staff.setStore(store);
            staff.setLastUpdate(LocalDateTime.now());
            staffRepository.save(staff);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating staff: " + e.getMessage(), e);
        }
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<StaffDTO>> getStaffByLastName(@PathVariable("ln") String lastName) {
        try {
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid last name: last name cannot be empty");
            }
            List<Staff> staffList = staffRepository.findByLastNameContainingIgnoreCase(lastName);
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff by last name: " + lastName, e);
        }
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<StaffDTO>> getStaffByFirstName(@PathVariable("fn") String firstName) {
        try {
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid first name: first name cannot be empty");
            }
            List<Staff> staffList = staffRepository.findByFirstNameContainingIgnoreCase(firstName);
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff by first name: " + firstName, e);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StaffDTO> getStaffByEmail(@PathVariable("email") String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid email: email cannot be empty");
            }
            Staff staff = staffRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for email: " + email));
            return ResponseEntity.ok(staffMapper.staffToDTO(staff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff by email: " + email, e);
        }
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<StaffDTO>> getStaffByCity(@PathVariable("city") String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid city: city name cannot be empty");
            }
            List<Staff> staffList = staffRepository.findByAddress_City_City(city);
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff by city: " + city, e);
        }
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<StaffDTO>> getStaffByCountry(@PathVariable("country") String country) {
        try {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid country: country name cannot be empty");
            }
            List<Staff> staffList = staffRepository.findByAddress_City_Country_Country(country);
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff by country: " + country, e);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<StaffDTO> getStaffByPhone(@PathVariable("phone") String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Staff staff = staffRepository.findByAddress_Phone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for phone: " + phone));
            return ResponseEntity.ok(staffMapper.staffToDTO(staff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff by phone: " + phone, e);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<StaffDTO>> getActiveStaff() {
        try {
            List<Staff> staffList = staffRepository.findByActiveTrue();
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching active staff", e);
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<StaffDTO>> getInactiveStaff() {
        try {
            List<Staff> staffList = staffRepository.findByActiveFalse();
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching inactive staff", e);
        }
    }

    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<StaffDTO>> getStaffByStore(@PathVariable("storeId") Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid storeId: must be a positive integer");
            }
            List<Staff> staffList = staffRepository.findByStore_StoreId(storeId);
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff for store ID: " + storeId, e);
        }
    }

    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<StaffDTO>> getActiveStaffByStore(@PathVariable("storeId") Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid storeId: must be a positive integer");
            }
            List<Staff> staffList = staffRepository.findByStore_StoreIdAndActive(storeId, true);
            if (staffList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staffList.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching active staff for store ID: " + storeId, e);
        }
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<StaffDTO> assignAddress(@PathVariable("id") Integer id, @RequestBody Integer addressId) {
        try {
            if (id <= 0 || addressId <= 0) {
                throw new IllegalArgumentException("Invalid IDs: staffId and addressId must be positive integers");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + id));
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Address not found for ID: " + addressId));
            staff.setAddress(address);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.staffToDTO(updatedStaff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning address to staff ID: " + id, e);
        }
    }

    @PutMapping("/update/fn/{id}")
    public ResponseEntity<StaffDTO> updateFirstName(@PathVariable("id") Integer id, @RequestBody String firstName) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staffId: must be a positive integer");
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid first name: first name cannot be empty");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + id));
            staff.setFirstName(firstName);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.staffToDTO(updatedStaff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating first name for staff ID: " + id, e);
        }
    }

    @PutMapping("/update/ln/{id}")
    public ResponseEntity<StaffDTO> updateLastName(@PathVariable("id") Integer id, @RequestBody String lastName) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staffId: must be a positive integer");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid last name: last name cannot be empty");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + id));
            staff.setLastName(lastName);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.staffToDTO(updatedStaff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating last name for staff ID: " + id, e);
        }
    }

    @PutMapping("/update/email/{id}")
    public ResponseEntity<StaffDTO> updateEmail(@PathVariable("id") Integer id, @RequestBody String email) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staffId: must be a positive integer");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid email: email cannot be empty");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + id));
            staff.setEmail(email);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.staffToDTO(updatedStaff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating email for staff ID: " + id, e);
        }
    }

    @PutMapping("/update/store/{id}")
    public ResponseEntity<StaffDTO> assignStore(@PathVariable("id") Integer id, @RequestBody Integer storeId) {
        try {
            if (id <= 0 || storeId <= 0) {
                throw new IllegalArgumentException("Invalid IDs: staffId and storeId must be positive integers");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + id));
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));
            staff.setStore(store);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.staffToDTO(updatedStaff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning store to staff ID: " + id, e);
        }
    }

    @PutMapping("/update/phone/{id}")
    public ResponseEntity<StaffDTO> updatePhone(@PathVariable("id") Integer id, @RequestBody String phone) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid staffId: must be a positive integer");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Staff staff = staffRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + id));
            Address address = staff.getAddress();
            if (address == null) {
                throw new IllegalArgumentException("Staff ID: " + id + " has no associated address");
            }
            address.setPhone(phone);
            address.setLastUpdate(LocalDateTime.now());
            addressRepository.save(address);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.staffToDTO(updatedStaff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating phone for staff ID: " + id, e);
        }
    }
}