package com.filmrental.controller;

import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.CityRepository;
import com.filmrental.repository.CountryRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StaffRepository;
import com.filmrental.repository.StoreRepository;
import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private StaffMapper staffMapper;

    @Autowired
    private StoreMapper storeMapper;

    @PutMapping("/customers/{id}/{addressId}")
    public ResponseEntity<CustomerDTO> assignAddressToCustomer(@PathVariable("id") Integer customerId, @PathVariable("addressId") Integer addressId) {
        try {
            if (customerId <= 0 || addressId <= 0) {
                throw new IllegalArgumentException("Invalid IDs: customerId and addressId must be positive integers");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + customerId));
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Address not found for ID: " + addressId));
            customer.setAddress(address);
            customer.setLastUpdate(LocalDateTime.now());
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning address to customer ID: " + customerId, e);
        }
    }

    @GetMapping("/customers/city/{city}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCity(@PathVariable("city") String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid city: city name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByAddress_City_City(city);
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customers for city: " + city, e);
        }
    }

    @GetMapping("/customers/country/{country}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCountry(@PathVariable("country") String country) {
        try {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid country: country name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByAddress_City_Country_Country(country);
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customers for country: " + country, e);
        }
    }

    @GetMapping("/customers/phone/{phone}")
    public ResponseEntity<CustomerDTO> getCustomerByPhone(@PathVariable("phone") String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Customer customer = customerRepository.findByAddress_Phone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for phone: " + phone));
            return ResponseEntity.ok(customerMapper.toDto(customer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customer for phone: " + phone, e);
        }
    }

    @PutMapping("/staff/{id}/{addressId}")
    public ResponseEntity<StaffDTO> assignAddressToStaff(@PathVariable("id") Integer staffId, @PathVariable("addressId") Integer addressId) {
        try {
            if (staffId <= 0 || addressId <= 0) {
                throw new IllegalArgumentException("Invalid IDs: staffId and addressId must be positive integers");
            }
            Staff staff = staffRepository.findById(staffId)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for ID: " + staffId));
            Address address = addressRepository.findById(addressId)
                    .orElseThrow(() -> new IllegalArgumentException("Address not found for ID: " + addressId));
            staff.setAddress(address);
            staff.setLastUpdate(LocalDateTime.now());
            Staff updatedStaff = staffRepository.save(staff);
            return ResponseEntity.ok(staffMapper.staffToDTO(updatedStaff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning address to staff ID: " + staffId, e);
        }
    }

    @GetMapping("/staff/city/{city}")
    public ResponseEntity<List<StaffDTO>> getStaffByCity(@PathVariable("city") String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid city: city name cannot be empty");
            }
            List<Staff> staff = staffRepository.findByAddress_City_City(city);
            if (staff.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staff.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff for city: " + city, e);
        }
    }

    @GetMapping("/staff/country/{country}")
    public ResponseEntity<List<StaffDTO>> getStaffByCountry(@PathVariable("country") String country) {
        try {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid country: country name cannot be empty");
            }
            List<Staff> staff = staffRepository.findByAddress_City_Country_Country(country);
            if (staff.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StaffDTO> staffDTOs = staff.stream()
                    .map(staffMapper::staffToDTO)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(staffDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff for country: " + country, e);
        }
    }

    @GetMapping("/staff/phone/{phone}")
    public ResponseEntity<StaffDTO> getStaffByPhone(@PathVariable("phone") String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Staff staff = staffRepository.findByAddress_Phone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Staff not found for phone: " + phone));
            return ResponseEntity.ok(staffMapper.staffToDTO(staff));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching staff for phone: " + phone, e);
        }
    }

    @PutMapping("/stores/{storeId}/{addressId}")
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

    @GetMapping("/stores/city/{city}")
    public ResponseEntity<List<StoreDTO>> getStoresByCity(@PathVariable("city") String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid city: city name cannot be empty");
            }
            List<Store> stores = storeRepository.findByAddress_City_City(city);
            if (stores.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<StoreDTO> storeDTOs = stores.stream()
                    .map(storeMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(storeDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching stores for city: " + city, e);
        }
    }

    @GetMapping("/stores/country/{country}")
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
            throw new IllegalArgumentException("Error fetching stores for country: " + country, e);
        }
    }

    @GetMapping("/stores/phone/{phone}")
    public ResponseEntity<StoreDTO> getStoreByPhone(@PathVariable("phone") String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Store store = storeRepository.findByAddress_Phone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for phone: " + phone));
            return ResponseEntity.ok(storeMapper.toDto(store));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching store for phone: " + phone, e);
        }
    }
}