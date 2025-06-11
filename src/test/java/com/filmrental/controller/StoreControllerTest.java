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
import com.filmrental.model.entity.Country;
import com.filmrental.model.entity.Customer;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.AddressRepository;
import com.filmrental.repository.CityRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StoreControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private StoreMapper storeMapper;

    @Mock
    private StaffMapper staffMapper;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private StoreController storeController;

    private Store store;
    private Address address;
    private City city;
    private Country country;
    private Staff staff;
    private Customer customer;
    private StoreDTO storeDTO;
    private StaffDTO staffDTO;
    private CustomerDTO customerDTO;
    private StoreManagerDTO storeManagerDTO;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();

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
        address.setPhone("1234567890");
        address.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        staff = new Staff();
        staff.setStaffId(1);
        staff.setFirstName("Priya");
        staff.setLastName("Sharma");
        staff.setEmail("priya.sharma@example.com");
        staff.setAddress(address);
        staff.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        store = new Store();
        store.setStoreId(1);
        store.setAddress(address);
        store.setManagerStaff(staff);
        store.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        customer = new Customer();
        customer.setCustomerId(1);
        customer.setFirstName("Amit");
        customer.setLastName("Patel");
        customer.setAddress(address);
        customer.setStore(store);
        customer.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        storeDTO = new StoreDTO();
        storeDTO.setStoreId(1);
        storeDTO.setAddressId(1);
        storeDTO.setManagerStaffId(1);
        storeDTO.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 36));

        staffDTO = new StaffDTO();
        staffDTO.setStaffId(1);
        staffDTO.setFirstName("Priya");
        staffDTO.setLastName("Sharma");

        customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(1);
        customerDTO.setFirstName("Amit");
        customerDTO.setLastName("Patel");

        storeManagerDTO = new StoreManagerDTO("Priya", "Sharma", "priya.sharma@example.com", "1234567890", "123 Mumbai St", "Mumbai");
    }

    @Test
    public void testAddStore_Success() throws Exception {
        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        when(staffRepository.findById(1)).thenReturn(Optional.of(staff));
        when(storeMapper.toEntity(any(StoreDTO.class))).thenReturn(store);
        when(storeRepository.save(any(Store.class))).thenReturn(store);

        mockMvc.perform(post("/api/store/post")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"storeId\":1,\"addressId\":1,\"managerStaffId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Record Created Successfully"));
    }


    @Test
    public void testGetStoreById_Success() throws Exception {
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        mockMvc.perform(get("/api/store/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(1))
                .andExpect(jsonPath("$.addressId").value(1))
                .andExpect(jsonPath("$.managerStaffId").value(1));
    }

    @Test
    public void testAssignAddressToStore_Success() throws Exception {
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(addressRepository.findById(2)).thenReturn(Optional.of(address));
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        mockMvc.perform(put("/api/store/1/address/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(1));
    }


    @Test
    public void testGetStoresByCity_Success() throws Exception {
        when(cityRepository.findByCity("Mumbai")).thenReturn(Optional.of(city));
        when(storeRepository.findByAddressCity(city)).thenReturn(Arrays.asList(store));
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        mockMvc.perform(get("/api/store/city/Mumbai")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeId").value(1));
    }

    @Test
    public void testGetStoresByCity_NotFound() throws Exception {
        when(cityRepository.findByCity("Mumbai")).thenReturn(Optional.of(city));
        when(storeRepository.findByAddressCity(city)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/store/city/Mumbai")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStoresByCountry_Success() throws Exception {
        when(storeRepository.findByAddress_City_Country_Country("India")).thenReturn(Arrays.asList(store));
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        mockMvc.perform(get("/api/store/country/India")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeId").value(1));
    }

    @Test
    public void testGetStoresByCountry_NotFound() throws Exception {
        when(storeRepository.findByAddress_City_Country_Country("India")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/store/country/India")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStoreByPhone_Success() throws Exception {
        when(storeRepository.findByAddress_Phone("1234567890")).thenReturn(Optional.of(store));
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        mockMvc.perform(get("/api/store/phone/1234567890")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(1));
    }


    @Test
    public void testUpdateStorePhone_Success() throws Exception {
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        mockMvc.perform(put("/api/store/update/1/9876543210")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(1));
    }


    @Test
    public void testAssignManagerToStore_Success() throws Exception {
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(staffRepository.findById(2)).thenReturn(Optional.of(staff));
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        mockMvc.perform(put("/api/store/1/manager/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storeId").value(1));
    }


    @Test
    public void testGetStaffByStore_Success() throws Exception {
        when(staffRepository.findByStore_StoreId(1)).thenReturn(Arrays.asList(staff));
        when(staffMapper.staffToDTO(staff)).thenReturn(staffDTO);

        mockMvc.perform(get("/api/store/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].staffId").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Priya"));
    }

    @Test
    public void testGetStaffByStore_NotFound() throws Exception {
        when(staffRepository.findByStore_StoreId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/store/staff/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetCustomersByStore_Success() throws Exception {
        when(customerRepository.findByStore_StoreId(1)).thenReturn(Arrays.asList(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDTO);

        mockMvc.perform(get("/api/store/customer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].firstName").value("Amit"));
    }

    @Test
    public void testGetCustomersByStore_NotFound() throws Exception {
        when(customerRepository.findByStore_StoreId(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/store/customer/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetManagerByStore_Success() throws Exception {
        when(storeRepository.findById(1)).thenReturn(Optional.of(store));
        when(staffMapper.staffToDTO(staff)).thenReturn(staffDTO);

        mockMvc.perform(get("/api/store/manager/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.staffId").value(1))
                .andExpect(jsonPath("$.firstName").value("Priya"));
    }


    @Test
    public void testGetAllManagersAndStoreDetails_Success() throws Exception {
        when(storeRepository.findAll()).thenReturn(Arrays.asList(store));

        mockMvc.perform(get("/api/store/managers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Priya"))
                .andExpect(jsonPath("$[0].email").value("priya.sharma@example.com"));
    }

    @Test
    public void testGetAllManagersAndStoreDetails_NotFound() throws Exception {
        when(storeRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/store/managers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}