package com.filmrental.controller;

import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StoreRepository;
import com.filmrental.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @PostMapping("/post")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            if (customerDTO == null || customerDTO.getFirstName() == null || customerDTO.getLastName() == null || customerDTO.getEmail() == null) {
                throw new IllegalArgumentException("Invalid customer data: firstName, lastName, and email are required");
            }
            if (customerDTO.getStoreId() == null || customerDTO.getAddressId() == null) {
                throw new IllegalArgumentException("Invalid customer data: storeId and addressId are required");
            }
            Store store = storeRepository.findById(customerDTO.getStoreId())
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + customerDTO.getStoreId()));
            Address address = addressRepository.findById(customerDTO.getAddressId())
                    .orElseThrow(() -> new IllegalArgumentException("Address not found for ID: " + customerDTO.getAddressId()));
            Customer customer = customerMapper.toEntity(customerDTO);
            customer.setStore(store);
            customer.setAddress(address);
            customer.setCreateDate(customerDTO.getCreateDate() != null ? customerDTO.getCreateDate() : java.time.LocalDate.now());
            customer.setLastUpdate(LocalDateTime.now());
            customer.setActive(customerDTO.getActive() != null ? customerDTO.getActive() : true);
            customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating customer: " + e.getMessage(), e);
        }
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByLastName(@PathVariable("ln") String lastName) {
        try {
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid last name: last name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByLastNameContainingIgnoreCase(lastName);
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customers by last name: " + lastName, e);
        }
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByFirstName(@PathVariable("fn") String firstName) {
        try {
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid first name: first name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCase(firstName);
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customers by first name: " + firstName, e);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable("email") String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid email: email cannot be empty");
            }
            Customer customer = customerRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for email: " + email));
            return ResponseEntity.ok(customerMapper.toDto(customer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customer by email: " + email, e);
        }
    }

    @PutMapping("/{id}/{addressId}")
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

    @GetMapping("/city/{city}")
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
            throw new IllegalArgumentException("Error fetching customers by city: " + city, e);
        }
    }

    @GetMapping("/country/{country}")
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
            throw new IllegalArgumentException("Error fetching customers by country: " + country, e);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        try {
            List<Customer> customers = customerRepository.findByActiveTrue();
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching active customers", e);
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<CustomerDTO>> getInactiveCustomers() {
        try {
            List<Customer> customers = customerRepository.findByActiveFalse();
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching inactive customers", e);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDTO> getCustomerByPhone(@PathVariable("phone") String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Customer customer = customerRepository.findByAddress_Phone(phone)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for phone: " + phone));
            return ResponseEntity.ok(customerMapper.toDto(customer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching customer by phone: " + phone, e);
        }
    }

    @PutMapping("/update/{id}/{fn}")
    public ResponseEntity<CustomerDTO> updateCustomerFirstName(@PathVariable("id") Integer customerId, @PathVariable("fn") String firstName) {
        try {
            if (customerId <= 0) {
                throw new IllegalArgumentException("Invalid customerId: must be a positive integer");
            }
            if (firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid first name: first name cannot be empty");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + customerId));
            customer.setFirstName(firstName);
            customer.setLastUpdate(LocalDateTime.now());
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating first name for customer ID: " + customerId, e);
        }
    }

    @PutMapping("/update/{id}/ln")
    public ResponseEntity<CustomerDTO> updateCustomerLastName(@PathVariable("id") Integer customerId, @RequestBody String lastName) {
        try {
            if (customerId <= 0) {
                throw new IllegalArgumentException("Invalid customerId: must be a positive integer");
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid last name: last name cannot be empty");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + customerId));
            customer.setLastName(lastName);
            customer.setLastUpdate(LocalDateTime.now());
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating last name for customer ID: " + customerId, e);
        }
    }

    @PutMapping("/update/{id}/email")
    public ResponseEntity<CustomerDTO> updateCustomerEmail(@PathVariable("id") Integer customerId, @RequestBody String email) {
        try {
            if (customerId <= 0) {
                throw new IllegalArgumentException("Invalid customerId: must be a positive integer");
            }
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid email: email cannot be empty");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + customerId));
            customer.setEmail(email);
            customer.setLastUpdate(LocalDateTime.now());
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating email for customer ID: " + customerId, e);
        }
    }

    @PutMapping("/update/{id}/store")
    public ResponseEntity<CustomerDTO> assignStoreToCustomer(@PathVariable("id") Integer customerId, @RequestBody Integer storeId) {
        try {
            if (customerId <= 0 || storeId <= 0) {
                throw new IllegalArgumentException("Invalid IDs: customerId and storeId must be positive integers");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + customerId));
            Store store = storeRepository.findById(storeId)
                    .orElseThrow(() -> new IllegalArgumentException("Store not found for ID: " + storeId));
            customer.setStore(store);
            customer.setLastUpdate(LocalDateTime.now());
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error assigning store to customer ID: " + customerId, e);
        }
    }

    @PutMapping("/update/{id}/phone")
    public ResponseEntity<CustomerDTO> updateCustomerPhone(@PathVariable("id") Integer customerId, @RequestBody String phone) {
        try {
            if (customerId <= 0) {
                throw new IllegalArgumentException("Invalid customerId: must be a positive integer");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid phone: phone number cannot be empty");
            }
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found for ID: " + customerId));
            Address address = customer.getAddress();
            if (address == null) {
                throw new IllegalArgumentException("Customer ID: " + customerId + " has no associated address");
            }
            address.setPhone(phone);
            address.setLastUpdate(LocalDateTime.now());
            addressRepository.save(address);
            customer.setLastUpdate(LocalDateTime.now());
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating phone for customer ID: " + customerId, e);
        }
    }

    @GetMapping("/store/{storeId}/active")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomersByStore(@PathVariable("storeId") Integer storeId) {
        try {
            if (storeId <= 0) {
                throw new IllegalArgumentException("Invalid storeId: must be a positive integer");
            }
            List<Customer> customers = customerRepository.findByStore_StoreIdAndActive(storeId, true);
            if (customers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<CustomerDTO> customerDTOs = customers.stream()
                    .map(customerMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(customerDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching active customers for store ID: " + storeId, e);
        }
    }
}