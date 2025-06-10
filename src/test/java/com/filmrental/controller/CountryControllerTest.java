package com.filmrental.controller;

import com.filmrental.mapper.CustomerMapper;
import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.CustomerDTO;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.*;
import com.filmrental.repository.CountryRepository;
import com.filmrental.repository.CustomerRepository;
import com.filmrental.repository.StaffRepository;
import com.filmrental.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CountryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private StaffMapper staffMapper;

    @Mock
    private StoreMapper storeMapper;

    @InjectMocks
    private CountryController countryController;

    private Country country;
    private Address address;
    private City city;
    private Customer customer;
    private Staff staff;
    private Store store;
    private CustomerDTO customerDTO;
    private StaffDTO staffDTO;
    private StoreDTO storeDTO;

    @BeforeEach
    public void setUp() {
        // Setup MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();

        // Setup test data
        country = new Country();
        country.setCountryId(1);
        country.setCountry("India");
        country.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        city = new City();
        city.setCityId(1);
        city.setCity("Mumbai");
        city.setCountry(country);
        city.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        address = new Address();
        address.setAddressId(1);
        address.setAddress("123 Mumbai St");
        address.setCity(city);
        address.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("Amit");
        customer.setLastName("Patel");
        customer.setAddress(address);
        customer.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        staff = new Staff();
        staff.setStaffId(1);
        staff.setFirstName("Priya");
        staff.setLastName("Sharma");
        staff.setAddress(address);
        staff.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));


        customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(1);
        customerDTO.setFirstName("Amit");
        customerDTO.setLastName("Patel");

        staffDTO = new StaffDTO();
        staffDTO.setStaffId(1);
        staffDTO.setFirstName("Priya");
        staffDTO.setLastName("Sharma");

        storeDTO = new StoreDTO();
        storeDTO.setStoreId(1);
        storeDTO.setAddressId(1);
        storeDTO.setManagerStaffId(1);
        storeDTO.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));
    }


    @Test
    public void testGetCustomersByCountry_NoCustomersFound() throws Exception {
        // Arrange
        when(countryRepository.findByCountryIgnoreCase("India")).thenReturn(Optional.of(country));
        when(customerRepository.findByAddressCityCountry(country)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/country/India/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetCustomersByCountry_Success() throws Exception {
        // Arrange
        when(countryRepository.findByCountryIgnoreCase("India")).thenReturn(Optional.of(country));
        when(customerRepository.findByAddressCityCountry(country)).thenReturn(Arrays.asList(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        // Act & Assert
        mockMvc.perform(get("/api/country/India/customers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Amit"))
                .andExpect(jsonPath("$[0].lastName").value("Patel"));
    }


    // Tests for GET /api/country/{country}/staff
    @Test
    public void testGetStaffByCountry_Success() throws Exception {
        // Arrange
        when(countryRepository.findByCountryIgnoreCase("India")).thenReturn(Optional.of(country));
        when(staffRepository.findByAddressCityCountry(country)).thenReturn(Arrays.asList(staff));
        when(staffMapper.staffToDTO(staff)).thenReturn(staffDTO);

        // Act & Assert
        mockMvc.perform(get("/api/country/India/staff")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].staffId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Priya"))
                .andExpect(jsonPath("$[0].lastName").value("Sharma"));
    }

    @Test
    public void testGetStaffByCountry_NoStaffFound() throws Exception {
        // Arrange
        when(countryRepository.findByCountryIgnoreCase("India")).thenReturn(Optional.of(country));
        when(staffRepository.findByAddressCityCountry(country)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/country/India/staff")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Tests for GET /api/country/{country}/stores
    @Test
    public void testGetStoresByCountry_Success() throws Exception {
        // Arrange
        when(countryRepository.findByCountryIgnoreCase("India")).thenReturn(Optional.of(country));
        when(storeRepository.findByAddressCityCountry(country)).thenReturn(Arrays.asList(store));
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        // Act & Assert
        mockMvc.perform(get("/api/country/India/stores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeId").value(1))
                .andExpect(jsonPath("$[0].addressId").value(1))
                .andExpect(jsonPath("$[0].managerStaffId").value(1));
    }

    @Test
    public void testGetStoresByCountry_NoStoresFound() throws Exception {
        // Arrange
        when(countryRepository.findByCountryIgnoreCase("India")).thenReturn(Optional.of(country));
        when(storeRepository.findByAddressCityCountry(country)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/country/India/stores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}