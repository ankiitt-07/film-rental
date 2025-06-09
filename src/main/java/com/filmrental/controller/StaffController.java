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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/post")
    public ResponseEntity<StaffDTO> createStaff(@RequestBody StaffDTO staffDTO) {
        Staff staff = StaffMapper.DTOToStaff(staffDTO);
        Staff savedStaff = staffRepository.save(staff);
        return ResponseEntity.ok(StaffMapper.staffToDTO(savedStaff));
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<StaffDTO>> getStaffByLastName(@PathVariable String ln) {
        List<StaffDTO> staffList = staffRepository.findByLastName(ln)
                .stream()
                .map(StaffMapper::staffToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<StaffDTO>> getStaffByFirstName(@PathVariable String fn) {
        List<StaffDTO> staffList = staffRepository.findByFirstName(fn)
                .stream()
                .map(StaffMapper::staffToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<StaffDTO> getStaffByEmail(@PathVariable String email) {
        Staff staff = staffRepository.findByEmail(email);
        return staff != null
                ? ResponseEntity.ok(StaffMapper.staffToDTO(staff))
                : ResponseEntity.notFound().build();
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<StaffDTO>> getStaffByCity(@PathVariable String city) {
        List<StaffDTO> staffList = staffRepository.findByAddress_City_City(city)
                .stream()
                .map(StaffMapper::staffToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<StaffDTO>> getStaffByCountry(@PathVariable String country) {
        List<StaffDTO> staffList = staffRepository.findByAddress_City_Country_Country(country)
                .stream()
                .map(StaffMapper::staffToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<List<StaffDTO>> getStaffByPhone(@PathVariable String phone) {
        List<StaffDTO> staffList = staffRepository.findByPhone(phone)
                .stream()
                .map(StaffMapper::staffToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(staffList);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<StaffDTO> assignAddress(@PathVariable Integer id, @RequestBody Integer addressId) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        staff.setAddress(address);
        Staff updatedStaff = staffRepository.save(staff);
        return ResponseEntity.ok(StaffMapper.staffToDTO(updatedStaff));
    }

    @PutMapping("/update/fn/{id}")
    public ResponseEntity<StaffDTO> updateFirstName(@PathVariable Integer id, @RequestBody String firstName) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        staff.setFirstName(firstName);
        Staff updatedStaff = staffRepository.save(staff);
        return ResponseEntity.ok(StaffMapper.staffToDTO(updatedStaff));
    }

    @PutMapping("/update/ln/{id}")
    public ResponseEntity<StaffDTO> updateLastName(@PathVariable Integer id, @RequestBody String lastName) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        staff.setLastName(lastName);
        Staff updatedStaff = staffRepository.save(staff);
        return ResponseEntity.ok(StaffMapper.staffToDTO(updatedStaff));
    }

    @PutMapping("/update/email/{id}")
    public ResponseEntity<StaffDTO> updateEmail(@PathVariable Integer id, @RequestBody String email) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        staff.setEmail(email);
        Staff updatedStaff = staffRepository.save(staff);
        return ResponseEntity.ok(StaffMapper.staffToDTO(updatedStaff));
    }

    @PutMapping("/update/store/{id}")
    public ResponseEntity<StaffDTO> assignStore(@PathVariable Integer id, @RequestBody Integer storeId) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        staff.setStore(store);
        Staff updatedStaff = staffRepository.save(staff);
        return ResponseEntity.ok(StaffMapper.staffToDTO(updatedStaff));
    }

    @PutMapping("/update/phone/{id}")
    public ResponseEntity<StaffDTO> updatePhone(@PathVariable Integer id, @RequestBody String phone) {
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Staff not found"));
        staff.setPhone(phone);
        Staff updatedStaff = staffRepository.save(staff);
        return ResponseEntity.ok(StaffMapper.staffToDTO(updatedStaff));
    }
}