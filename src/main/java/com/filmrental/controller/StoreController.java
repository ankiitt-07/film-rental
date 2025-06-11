package com.filmrental.controller;

import com.filmrental.mapper.AddressMapper;
import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.AddressDTO;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.dto.StoreManagerDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.CustomerRepository;
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
@RequestMapping("/api/store")
public class StoreController {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StoreMapper storeMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<StoreDTO>> getAllStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Store> stores = storeRepository.findAll(pageable);
            return stores.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(stores.map(storeMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/post")
    public ResponseEntity<String> addStore(@RequestBody StoreDTO storeDTO) {
        try {
            // Validate required fields
            if (storeDTO.getFirstName() == null || storeDTO.getFirstName().trim().isEmpty() ||
                    storeDTO.getLastName() == null || storeDTO.getLastName().trim().isEmpty() ||
                    storeDTO.getAddressId() == null ||
                    storeDTO.getEmail() == null || storeDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("First name, last name, address ID, and email are required");
            }

            // Verify address exists
            Address address = addressRepository.findById(storeDTO.getAddressId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + storeDTO.getAddressId()));

            // Check if email is unique
            if (staffRepository.findByEmail(storeDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already in use: " + storeDTO.getEmail());
            }

            // Create new Staff (manager)
            Staff managerStaff = new Staff();
            managerStaff.setFirstName(storeDTO.getFirstName());
            managerStaff.setLastName(storeDTO.getLastName());
            managerStaff.setEmail(storeDTO.getEmail());
            managerStaff.setAddress(address);
            managerStaff.setActive(true); // Default
            managerStaff.setUsername(storeDTO.getEmail().split("@")[0]); // Default username
            managerStaff.setPassword("defaultPassword"); // Placeholder; use proper hashing in production
            managerStaff.setLastUpdate(LocalDateTime.now());
            Staff savedStaff = staffRepository.save(managerStaff);

            // Create new Store
            Store store = new Store();
            store.setManagerStaff(savedStaff);
            store.setAddress(address);
            store.setLastUpdate(LocalDateTime.now());
            storeRepository.save(store);

            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating store: " + e.getMessage());
        }
    }

    @PutMapping("/{storeId}/address/{addressId}")
    public ResponseEntity<StoreDTO> assignAddressToStore(@PathVariable Integer storeId, @PathVariable Integer addressId) {
        try {
            if (storeId <= 0 || addressId <= 0) {
                throw new IllegalArgumentException("Invalid store or address ID");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));
            store.setAddress(address);
            store.setLastUpdate(LocalDateTime.now());
            Store updatedStore = storeRepository.save(store);
            return ResponseEntity.ok(storeMapper.toDto(updatedStore));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<StoreDTO>> getStoresByCity(@PathVariable String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("City cannot be empty");
            }
            List<Store> stores = storeRepository.findByAddressCityCityContainingIgnoreCase(city);
            List<StoreDTO> storeDTOs = stores.stream()
                    .map(storeMapper::toDto)
                    .collect(Collectors.toList());
            return stores.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(storeDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<StoreDTO>> getStoresByCountry(@PathVariable String country) {
        try {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("Country cannot be empty");
            }
            List<Store> stores = storeRepository.findByAddressCityCountryCountryContainingIgnoreCase(country);
            List<StoreDTO> storeDTOs = stores.stream()
                    .map(storeMapper::toDto)
                    .collect(Collectors.toList());
            return stores.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(storeDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<StoreDTO> getStoreByPhone(@PathVariable String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be empty");
            }
            Store store = storeRepository.findByAddressPhone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with phone: " + phone));
            return ResponseEntity.ok(storeMapper.toDto(store));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{storeId}/{phone}")
    public ResponseEntity<StoreDTO> updateStorePhone(@PathVariable Integer storeId, @PathVariable String phone) {
        try {
            if (storeId <= 0 || phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid store ID or phone number");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
            Address address = store.getAddress();
            address.setPhone(phone);
            address.setLastUpdate(LocalDateTime.now());
            addressRepository.save(address);
            store.setLastUpdate(LocalDateTime.now());
            Store updatedStore = storeRepository.save(store);
            return ResponseEntity.ok(storeMapper.toDto(updatedStore));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{storeId}/manager/{managerStaffId}")
    public ResponseEntity<StoreDTO> assignManagerToStore(@PathVariable Integer storeId, @PathVariable Integer managerStaffId) {
        try {
            if (storeId <= 0 || managerStaffId <= 0) {
                throw new IllegalArgumentException("Invalid store or staff ID");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
            Staff managerStaff = staffRepository.findById(managerStaffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found with ID: " + managerStaffId));
            store.setManagerStaff(managerStaff);
            store.setLastUpdate(LocalDateTime.now());
            Store updatedStore = storeRepository.save(store);
            return ResponseEntity.ok(storeMapper.toDto(updatedStore));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/staff/{storeId}")
    public ResponseEntity<List<StaffDTO>> getStaffByStoreId(@PathVariable Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
            List<StaffDTO> staffDTOs = store.getStaff().stream()
                    .map(staffMapper::toDto)
                    .collect(Collectors.toList());
            return staffDTOs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(staffDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/customer/{storeId}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByStoreId(@PathVariable Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
            List<CustomerDTO> customerDTOs = store.getCustomers().stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return customerDTOs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customerDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/manager/{storeId}")
    public ResponseEntity<StaffDTO> getManagerByStoreId(@PathVariable Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid store ID");
            }
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
            StaffDTO staffDTO = staffMapper.toDto(store.getManagerStaff());
            return ResponseEntity.ok(staffDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/managers")
    public ResponseEntity<List<StoreManagerDTO>> getAllManagersAndStoreDetails() {
        try {
            List<Store> stores = storeRepository.findAll();
            List<StoreManagerDTO> storeManagerDTOs = stores.stream()
                    .map(store -> {
                        StoreManagerDTO dto = new StoreManagerDTO();
                        Staff manager = store.getManagerStaff();
                        Address address = store.getAddress();
                        dto.setFirstName(manager != null ? manager.getFirstName() : null);
                        dto.setLastName(manager != null ? manager.getLastName() : null);
                        dto.setEmail(manager != null ? manager.getEmail() : null);
                        dto.setPhone(address != null ? address.getPhone() : null);
                        dto.setAddress(address != null ? address.getAddress() : null);
                        dto.setCity(address != null && address.getCity() != null ? address.getCity().getCity() : null);
                        return dto;
                    })
                    .collect(Collectors.toList());
            return storeManagerDTOs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(storeManagerDTOs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}