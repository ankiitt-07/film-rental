//package com.filmrental.controller;
//
//import com.filmrental.mapper.AddressMapper;
//import com.filmrental.mapper.CustomerMapper;
//import com.filmrental.model.dto.CustomerDTO;
//import com.filmrental.model.entity.Address;
//import com.filmrental.model.entity.Customer;
//import com.filmrental.model.entity.Store;
//import com.filmrental.repository.AddressRepository;
//import com.filmrental.repository.CustomerRepository;
//import com.filmrental.repository.StoreRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/customers")
//public class CustomerController {
//
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private AddressRepository addressRepository;
//
//    @Autowired
//    private StoreRepository storeRepository;
//
//    @Autowired
//    private CustomerMapper customerMapper;
//
//    @Autowired
//    private AddressMapper addressMapper;
//
//    @GetMapping("/all")
//    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        try {
//            Pageable pageable = PageRequest.of(page, size);
//            Page<Customer> customers = customerRepository.findAll(pageable);
//            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.map(customerMapper::toDto));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
////    @PostMapping("/post")
////    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {
////        try {
////            if (customerDTO.getFirstName() == null || customerDTO.getLastName() == null || customerDTO.getEmail() == null) {
////                throw new IllegalArgumentException("First name, last name, and email are required");
////            }
////            if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
////                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
////            }
////            Customer customer = customerMapper.toEntity(customerDTO);
////            customer.setCreateDate(LocalDate.now());
////            customer.setLastUpdate(LocalDateTime.now());
////            customerRepository.save(customer);
////            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
////        } catch (IllegalArgumentException e) {
////            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
////        } catch (Exception e) {
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating customer");
////        }
////    }
//@PostMapping("/post")
//public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {
//    try {
//        if (customerDTO.getFirstName() == null || customerDTO.getLastName() == null || customerDTO.getEmail() == null) {
//            throw new IllegalArgumentException("First name, last name, and email are required");
//        }
//
//        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
//        }
//
//        Customer customer = customerMapper.toEntity(customerDTO);
//
//        // Fetch store and address
//        Store store = storeRepository.findById(customerDTO.getStoreId())
//                .orElseThrow(() -> new IllegalArgumentException("Store not found"));
//        Address address = addressRepository.findById(customerDTO.getAddressId())
//                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
//
//        customer.setStore(store);
//        customer.setAddress(address);
//
//        customer.setCreateDate(LocalDate.now());
//        customerRepository.save(customer);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
//    } catch (IllegalArgumentException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//    } catch (Exception e) {
//        e.printStackTrace(); // Log actual issue for now
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating customer");
//    }
//}
//
//
//    @GetMapping("/lastname/{ln}")
//    public ResponseEntity<List<CustomerDTO>> getCustomersByLastName(@PathVariable String ln) {
//        try {
//            if (ln == null || ln.trim().isEmpty()) {
//                throw new IllegalArgumentException("Last name cannot be empty");
//            }
//            List<Customer> customers = customerRepository.findByLastNameContainingIgnoreCase(ln);
//            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/firstname/{fn}")
//    public ResponseEntity<List<CustomerDTO>> getCustomersByFirstName(@PathVariable String fn) {
//        try {
//            if (fn == null || fn.trim().isEmpty()) {
//                throw new IllegalArgumentException("First name cannot be empty");
//            }
//            List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCase(fn);
//            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/email/{email}")
//    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
//        try {
//            if (email == null || email.trim().isEmpty()) {
//                throw new IllegalArgumentException("Email cannot be empty");
//            }
//            Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Customer not found with email: " + email));
//            return ResponseEntity.ok(customerMapper.toDto(customer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/{id}/{addressId}")
//    public ResponseEntity<CustomerDTO> assignAddressToCustomer(@PathVariable Integer id, @PathVariable Integer addressId) {
//        try {
//            if (id <= 0 || addressId <= 0) {
//                throw new IllegalArgumentException("Invalid customer or address ID");
//            }
//            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
//            Address address = addressRepository.findById(addressId).orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));
//            customer.setAddress(address);
//            customer.setLastUpdate(LocalDateTime.now());
//            Customer updatedCustomer = customerRepository.save(customer);
//            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/city/{city}")
//    public ResponseEntity<List<CustomerDTO>> getCustomersByCity(@PathVariable String city) {
//        try {
//            if (city == null || city.trim().isEmpty()) {
//                throw new IllegalArgumentException("City name cannot be empty");
//            }
//            List<Customer> customers = customerRepository.findByAddress_City_City(city);
//            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/country/{country}")
//    public ResponseEntity<List<CustomerDTO>> getCustomersByCountry(@PathVariable String country) {
//        try {
//            if (country == null || country.trim().isEmpty()) {
//                throw new IllegalArgumentException("Country name cannot be empty");
//            }
//            List<Customer> customers = customerRepository.findByAddress_City_Country_Country(country);
//            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/active")
//    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
//        try {
//            List<Customer> customers = customerRepository.findByActiveTrue();
//            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/inactive")
//    public ResponseEntity<List<CustomerDTO>> getInactiveCustomers() {
//        try {
//            List<Customer> customers = customerRepository.findByActiveFalse();
//            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @GetMapping("/phone/{phone}")
//    public ResponseEntity<CustomerDTO> getCustomerByPhone(@PathVariable String phone) {
//        try {
//            if (phone == null || phone.trim().isEmpty()) {
//                throw new IllegalArgumentException("Phone number cannot be empty");
//            }
//            Customer customer = customerRepository.findByAddress_Phone(phone).orElseThrow(() -> new IllegalArgumentException("Customer not found with phone: " + phone));
//            return ResponseEntity.ok(customerMapper.toDto(customer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/update/{id}/firstname")
//    public ResponseEntity<CustomerDTO> updateFirstName(@PathVariable Integer id, @RequestBody String firstName) {
//        try {
//            if (id <= 0 || firstName == null || firstName.trim().isEmpty()) {
//                throw new IllegalArgumentException("Invalid ID or first name");
//            }
//            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
//            customer.setFirstName(firstName);
//            customer.setLastUpdate(LocalDateTime.now());
//            Customer updatedCustomer = customerRepository.save(customer);
//            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/update/{id}/lastname")
//    public ResponseEntity<CustomerDTO> updateLastName(@PathVariable Integer id, @RequestBody String lastName) {
//        try {
//            if (id <= 0 || lastName == null || lastName.trim().isEmpty()) {
//                throw new IllegalArgumentException("Invalid ID or last name");
//            }
//            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
//            customer.setLastName(lastName);
//            customer.setLastUpdate(LocalDateTime.now());
//            Customer updatedCustomer = customerRepository.save(customer);
//            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/update/{id}/email")
//    public ResponseEntity<CustomerDTO> updateEmail(@PathVariable Integer id, @RequestBody String email) {
//        try {
//            if (id <= 0 || email == null || email.trim().isEmpty()) {
//                throw new IllegalArgumentException("Invalid ID or email");
//            }
//            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
//            customer.setEmail(email);
//            customer.setLastUpdate(LocalDateTime.now());
//            Customer updatedCustomer = customerRepository.save(customer);
//            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/update/{id}/store")
//    public ResponseEntity<CustomerDTO> assignStore(@PathVariable Integer id, @RequestBody Integer storeId) {
//        try {
//            if (id <= 0 || storeId <= 0) {
//                throw new IllegalArgumentException("Invalid customer or store ID");
//            }
//            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
//            Store store = storeRepository.findById(storeId).orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + storeId));
//            customer.setStore(store);
//            customer.setLastUpdate(LocalDateTime.now());
//            Customer updatedCustomer = customerRepository.save(customer);
//            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//
//    @PutMapping("/update/{id}/phone")
//    public ResponseEntity<CustomerDTO> updatePhone(@PathVariable Integer id, @RequestBody String phone) {
//        try {
//            if (id <= 0 || phone == null || phone.trim().isEmpty()) {
//                throw new IllegalArgumentException("Invalid ID or phone number");
//            }
//            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
//            Address address = customer.getAddress();
//            if (address == null) {
//                throw new IllegalArgumentException("Customer has no address");
//            }
//            address.setPhone(phone);
//            address.setLastUpdate(LocalDateTime.now());
//            addressRepository.save(address);
//            customer.setLastUpdate(LocalDateTime.now());
//            Customer updatedCustomer = customerRepository.save(customer);
//            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
//}
package com.filmrental.controller;

import com.filmrental.mapper.AddressMapper;
import com.filmrental.mapper.CustomerMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @Autowired
    private AddressMapper addressMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<CustomerDTO>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Customer> customers = customerRepository.findAll(pageable);
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.map(customerMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/all-list")
    public ResponseEntity<List<CustomerDTO>> getAllCustomersList() {
        try {
            List<Customer> customers = customerRepository.findAll();
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/post")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customerDTO) {
        try {
            if (customerDTO.getFirstName() == null || customerDTO.getLastName() == null || customerDTO.getEmail() == null) {
                throw new IllegalArgumentException("First name, last name, and email are required");
            }
            if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            }
            Customer customer = customerMapper.toEntity(customerDTO);
            customer.setCreateDate(LocalDate.now().atStartOfDay());
            customer.setLastUpdate(LocalDateTime.now());
            customerRepository.save(customer);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating customer");
        }
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByLastName(@PathVariable String ln) {
        try {
            if (ln == null || ln.trim().isEmpty()) {
                throw new IllegalArgumentException("Last name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByLastNameContainingIgnoreCase(ln);
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByFirstName(@PathVariable String fn) {
        try {
            if (fn == null || fn.trim().isEmpty()) {
                throw new IllegalArgumentException("First name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCase(fn);
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be empty");
            }
            Customer customer = customerRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Customer not found with email: " + email));
            return ResponseEntity.ok(customerMapper.toDto(customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid customer ID");
            }
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
            return ResponseEntity.ok(customerMapper.toDto(customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/{addressId}")
    public ResponseEntity<CustomerDTO> assignAddressToCustomer(@PathVariable Integer id, @PathVariable Integer addressId) {
        try {
            if (id <= 0 || addressId <= 0) {
                throw new IllegalArgumentException("Invalid customer or address ID");
            }
            Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));
            Address address = addressRepository.findById(addressId).orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + addressId));
            customer.setAddress(address);
            customer.setLastUpdate(LocalDateTime.now());
            Customer updatedCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updatedCustomer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCity(@PathVariable String city) {
        try {
            if (city == null || city.trim().isEmpty()) {
                throw new IllegalArgumentException("City name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByAddress_City_City(city);
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<CustomerDTO>> getCustomersByCountry(@PathVariable String country) {
        try {
            if (country == null || country.trim().isEmpty()) {
                throw new IllegalArgumentException("Country name cannot be empty");
            }
            List<Customer> customers = customerRepository.findByAddress_City_Country_Country(country);
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        try {
            List<Customer> customers = customerRepository.findByActiveTrue();
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<CustomerDTO>> getInactiveCustomers() {
        try {
            List<Customer> customers = customerRepository.findByActiveFalse();
            return customers.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(customers.stream().map(customerMapper::toDto).collect(Collectors.toList()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<CustomerDTO> getCustomerByPhone(@PathVariable String phone) {
        try {
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be empty");
            }
            Customer customer = customerRepository.findByAddress_Phone(phone).orElseThrow(() -> new IllegalArgumentException("Customer not found with phone: " + phone));
            return ResponseEntity.ok(customerMapper.toDto(customer));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO customerDTO) {
        try {
            // Validate ID and essential fields
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid customer ID");
            }
            if (customerDTO.getFirstName() == null || customerDTO.getFirstName().trim().isEmpty()
                    || customerDTO.getLastName() == null || customerDTO.getLastName().trim().isEmpty()
                    || customerDTO.getEmail() == null || customerDTO.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("First name, last name, and email are required");
            }

            // Fetch customer
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + id));

            // Email conflict check
            if (!customer.getEmail().equals(customerDTO.getEmail())
                    && customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }

            // Update basic fields
            customer.setFirstName(customerDTO.getFirstName());
            customer.setLastName(customerDTO.getLastName());
            customer.setEmail(customerDTO.getEmail());
            customer.setActive(customerDTO.getActive() != null ? customerDTO.getActive() : customer.getActive());

            // Update store
            if (customerDTO.getStoreId() != null) {
                Store store = storeRepository.findById(customerDTO.getStoreId())
                        .orElseThrow(() -> new IllegalArgumentException("Store not found with ID: " + customerDTO.getStoreId()));
                customer.setStore(store);
            }

            // Update address and phone
            if (customerDTO.getAddressId() != null) {
                Address address = addressRepository.findById(customerDTO.getAddressId())
                        .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + customerDTO.getAddressId()));
                customer.setAddress(address);

            }

            // Timestamp
            customer.setLastUpdate(LocalDateTime.now());

            // Save and return
            Customer updated = customerRepository.save(customer);
            return ResponseEntity.ok(customerMapper.toDto(updated));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}