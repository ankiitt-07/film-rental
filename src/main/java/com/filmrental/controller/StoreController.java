package com.filmrental.controller;

import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.dto.StoreManagerDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.City;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.CityRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StaffRepository;
import com.filmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping("/post")
    public ResponseEntity<String> addStore(@RequestBody StoreDTO dto) {
        try {
            if (dto == null || dto.getAddressId() == null || dto.getManagerStaffId() == null) {
                throw new IllegalArgumentException("Invalid store data: addressId and managerStaffId are required");
            }
            if (dto.getAddressId() <= 0 || dto.getManagerStaffId() <= 0) {
                throw new IllegalArgumentException("Invalid store data: addressId and managerStaffId must be positive integers");
            }
            Address address = addressRepository.findById(dto.getAddressId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found for ID: " + dto.getAddressId()));
            Staff manager = staffRepository.findById(dto.getManagerStaffId())
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + dto.getManagerStaffId()));
            Store store = storeMapper.toEntity(dto);
            store.setAddress(address);
            store.setManagerStaff(manager);
            store.setLastUpdate(LocalDateTime.now());
            storeRepository.save(store);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating store: " + e.getMessage(), e);
        }
    }

    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable("storeId") Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid storeId: must be a positive integer");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));
            return ResponseEntity.ok(storeMapper.toDto(store));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching store by ID: " + storeId, e);
        }
    }

    @PutMapping("/{storeId}/address/{addressId}")
    public ResponseEntity<StoreDTO> assignAddressToStore(@PathVariable("storeId") Integer storeId, @PathVariable("addressId") Integer addressId) {
        try {
            if (storeId <= 0 || addressId <= 0) {
                throw new IllegalArgumentException("Invalid IDs: storeId and addressId must be positive integers");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Address not found for ID: " + addressId));
            store.setAddress(address);
            store.setLastUpdate(LocalDateTime.now());
            Store updatedStore = storeRepository.save(store);
            return ResponseEntity.ok(storeMapper.toDto(updatedStore));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning address to store ID: " + storeId, e);
        }
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<StoreDTO>> getStoresByCity(@PathVariable("city") String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid city: city name cannot be empty");
            }
            City cityEntity = cityRepository.findByCity(city)
                    .orElseThrow(() -> new IllegalArgumentException("City not found: " + city));
            List<Store> stores = storeRepository.findByAddressCity(cityEntity);
            if (stores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StoreDTO> storeDTOs = stores.stream()
                    .map(storeMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(storeDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching stores by city: " + city, e);
        }
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<StoreDTO>> getStoresByCountry(@PathVariable("country") String country) {
        try {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid country: country name cannot be empty");
            }
            List<Store> stores = storeRepository.findByAddress_City_Country_Country(country);
            if (stores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StoreDTO> storeDTOs = stores.stream()
                    .map(storeMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(storeDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching stores by country: " + country, e);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<StoreDTO> getStoreByPhone(@PathVariable("phone") String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Store store = storeRepository.findByAddress_Phone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for phone: " + phone));
            return ResponseEntity.ok(storeMapper.toDto(store));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching store by phone: " + phone, e);
        }
    }

    @PutMapping("/update/{storeId}/{phone}")
    public ResponseEntity<StoreDTO> updateStorePhone(@PathVariable("storeId") Integer storeId, @PathVariable("phone") String phone) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid storeId: must be a positive integer");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));
            Address address = store.getAddress();
            if (address == null) {
                throw new IllegalArgumentException("Store ID: " + storeId + " has no associated address");
            }
            address.setPhone(phone);
            address.setLastUpdate(LocalDateTime.now());
            addressRepository.save(address);
            store.setLastUpdate(LocalDateTime.now());
            Store updatedStore = storeRepository.save(store);
            return ResponseEntity.ok(storeMapper.toDto(updatedStore));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating phone for store ID: " + storeId, e);
        }
    }

    @PutMapping("/{storeId}/manager/{managerStaffId}")
    public ResponseEntity<StoreDTO> assignManagerToStore(@PathVariable("storeId") Integer storeId, @PathVariable("managerStaffId") Integer managerStaffId) {
        try {
            if (storeId <= 0 || managerStaffId <= 0) {
                throw new IllegalArgumentException("Invalid IDs: storeId and managerStaffId must be positive integers");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));
            Staff manager = staffRepository.findById(managerStaffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + managerStaffId));
            store.setManagerStaff(manager);
            store.setLastUpdate(LocalDateTime.now());
            Store updatedStore = storeRepository.save(store);
            return ResponseEntity.ok(storeMapper.toDto(updatedStore));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning manager to store ID: " + storeId, e);
        }
    }

    @GetMapping("/staff/{storeId}")
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

    @GetMapping("/customer/{storeId}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByStore(@PathVariable("storeId") Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid storeId: must be a positive integer");
            }
            List<CustomerDTO> customers = customerRepository.findByStore_StoreId(storeId)
                    .stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customers for store ID: " + storeId, e);
        }
    }

    @GetMapping("/manager/{storeId}")
    public ResponseEntity<StaffDTO> getManagerByStore(@PathVariable("storeId") Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid storeId: must be a positive integer");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));
            Staff manager = store.getManagerStaff();
            if (manager == null) {
                throw new IllegalArgumentException("No manager assigned to store ID: " + storeId);
            }
            return ResponseEntity.ok(staffMapper.staffToDTO(manager));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching manager for store ID: " + storeId, e);
        }
    }

    @GetMapping("/managers")
    public ResponseEntity<List<StoreManagerDTO>> getAllManagersAndStoreDetails() {
        try {
            List<Store> stores = storeRepository.findAll();
            if (stores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StoreManagerDTO> managerDetails = stores.stream()
                    .filter(store -> store.getManagerStaff() != null)
                    .map(store -> {
                        Staff manager = store.getManagerStaff();
                        Address address = store.getAddress();
                        return new StoreManagerDTO(
                                manager.getFirstName(),
                                manager.getLastName(),
                                manager.getEmail(),
                                address != null ? address.getPhone() : null,
                                address != null ? address.getAddress() : null,
                                address != null && address.getCity() != null ? address.getCity().getCity() : null
                        );
                    })
                    .collect(Collectors.toList());
            if (managerDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(managerDetails);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching managers and store details", e);
        }
    }
}