package com.filmrental.controller;

import com.filmrental.exception.ResourceNotFoundException;
import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.StaffRepository;
import com.filmrental.repository.StoreRepository;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.util.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StaffMapper staffMapper;

    @PostMapping("/post")
    public ResponseEntity<?> addStaff(@Validated(StaffDTO.Create.class) @RequestBody StaffDTO dto) {
        if (staffRepository.findByEmail(dto.getEmail()) != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.EMAIL_ALREADY_EXISTS);
            return ResponseEntity.status(400).body(errorResponse);
        }
        if (staffRepository.findByPhone(dto.getPhone()) != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.PHONE_ALREADY_EXISTS);
            return ResponseEntity.status(400).body(errorResponse);
        }

        Staff staff = staffMapper.DTOToStaff(dto);

        if (dto.getStore_id() != null) {
            Store store = storeRepository.findById(dto.getStore_id())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.INVALID_STORE_ID));
            staff.setStore(store);
        }

        if (dto.getAddress_id() != null) {
            Address address = addressRepository.findById(dto.getAddress_id())
                    .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.INVALID_ADDRESS_ID));
            staff.setAddress(address);
        }

        staffRepository.save(staff);
        Map<String, String> successResponse = new HashMap<>();
        successResponse.put("message", "Record Created Successfully");
        return ResponseEntity.status(201).body(successResponse);
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<?> searchByFirstName(@PathVariable String fn) {
        if (fn == null || fn.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        List<StaffDTO> staffList = staffRepository.findByFirstNameContainingIgnoreCase(fn)
                .stream()
                .map(staffMapper::staffToDTO)
                .toList();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<?> searchByLastName(@PathVariable String ln) {
        if (ln == null || ln.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        List<StaffDTO> staffList = staffRepository.findByLastNameContainingIgnoreCase(ln)
                .stream()
                .map(staffMapper::staffToDTO)
                .toList();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        if (email == null || email.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findByEmail(email);
        if (staff == null) {
            throw new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND);
        }
        return ResponseEntity.ok(staffMapper.staffToDTO(staff));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<?> getByPhone(@PathVariable String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findByPhone(phone);
        if (staff == null) {
            throw new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND);
        }
        return ResponseEntity.ok(staffMapper.staffToDTO(staff));
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<?> searchByCity(@PathVariable String city) {
        if (city == null || city.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        List<StaffDTO> staffList = staffRepository.findByCity(city)
                .stream()
                .map(staffMapper::staffToDTO)
                .toList();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<?> searchByCountry(@PathVariable String country) {
        if (country == null || country.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        List<StaffDTO> staffList = staffRepository.findByCountry(country)
                .stream()
                .map(staffMapper::staffToDTO)
                .toList();
        return ResponseEntity.ok(staffList);
    }

    @PutMapping("/update/fn/{id}")
    public ResponseEntity<?> updateFirstName(@PathVariable Long id, @RequestBody StaffDTO dto) {
        if (dto.getFirst_name() == null || dto.getFirst_name().trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND));
        staff.setFirst_name(dto.getFirst_name());
        staffRepository.save(staff);
        return ResponseEntity.ok(staffMapper.staffToDTO(staff));
    }

    @PutMapping("/update/ln/{id}")
    public ResponseEntity<?> updateLastName(@PathVariable Long id, @RequestBody StaffDTO dto) {
        if (dto.getLast_name() == null || dto.getLast_name().trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND));
        staff.setLast_name(dto.getLast_name());
        staffRepository.save(staff);
        return ResponseEntity.ok(staffMapper.staffToDTO(staff));
    }

    @PutMapping("/update/email/{id}")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @RequestBody StaffDTO dto) {
        if (dto.getEmail() == null || !dto.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND));
        if (!dto.getEmail().equals(staff.getEmail()) && staffRepository.findByEmail(dto.getEmail()) != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.EMAIL_ALREADY_EXISTS);
            return ResponseEntity.status(400).body(errorResponse);
        }
        staff.setEmail(dto.getEmail());
        staffRepository.save(staff);
        return ResponseEntity.ok(staffMapper.staffToDTO(staff));
    }

    @PutMapping("/update/phone/{id}")
    public ResponseEntity<?> updatePhone(@PathVariable Long id, @RequestBody StaffDTO dto) {
        if (dto.getPhone() == null || !dto.getPhone().matches("\\d{10}")) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_INPUT);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND));
        if (!dto.getPhone().equals(staff.getPhone()) && staffRepository.findByPhone(dto.getPhone()) != null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.PHONE_ALREADY_EXISTS);
            return ResponseEntity.status(400).body(errorResponse);
        }
        staff.setPhone(dto.getPhone());
        staffRepository.save(staff);
        return ResponseEntity.ok(staffMapper.staffToDTO(staff));
    }

    @PutMapping("/update/store/{id}")
    public ResponseEntity<?> assignStore(@PathVariable Long id, @RequestBody StaffDTO dto) {
        if (dto.getStore_id() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_STORE_ID);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND));
        Store store = storeRepository.findById(dto.getStore_id())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.INVALID_STORE_ID));
        staff.setStore(store);
        staffRepository.save(staff);
        return ResponseEntity.ok(staffMapper.staffToDTO(staff));
    }

    @PutMapping(value = "/{id}/address", consumes = "application/json")
    public ResponseEntity<?> assignAddress(@PathVariable Long id, @RequestBody StaffDTO dto) {
        if (dto.getAddress_id() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", ErrorMessages.INVALID_ADDRESS_ID);
            return ResponseEntity.status(400).body(errorResponse);
        }
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.STAFF_NOT_FOUND));
        Address address = addressRepository.findById(dto.getAddress_id())
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.INVALID_ADDRESS_ID));
        staff.setAddress(address);
        staffRepository.save(staff);
        return ResponseEntity.ok(staffMapper.addressToDTO(address));
    }
}