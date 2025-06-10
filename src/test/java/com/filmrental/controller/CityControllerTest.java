package com.filmrental.controller;

import com.filmrental.mapper.StaffMapper;
import com.filmrental.mapper.StoreMapper;
import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.Address;
import com.filmrental.model.entity.City;
import com.filmrental.model.entity.Staff;
import com.filmrental.model.entity.Store;
import com.filmrental.repository.CityRepository;
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
public class CityControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private StoreMapper storeMapper;

    @Mock
    private StaffMapper staffMapper;

    @InjectMocks
    private CityController cityController;

    private City city;
    private Address address;
    private Staff staff;
    private Store store;
    private StaffDTO staffDTO;
    private StoreDTO storeDTO;

    @BeforeEach
    public void setUp() {
        // Setup MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(cityController).build();

        // Setup test data
        city = new City();
        city.setCityId(1);
        city.setCity("Mumbai");
        city.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 58));

        address = new Address();
        address.setAddressId(1);
        address.setAddress("123 Mumbai St");
        address.setCity(city);
        address.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 58));

        staff = new Staff();
        staff.setStaffId(1);
        staff.setFirstName("Priya");
        staff.setLastName("Sharma");
        staff.setAddress(address);
        staff.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 58));


        staffDTO = new StaffDTO();
        staffDTO.setStaffId(1);
        staffDTO.setFirstName("Priya");
        staffDTO.setLastName("Sharma");

        storeDTO = new StoreDTO();
        storeDTO.setStoreId(1);
        storeDTO.setAddressId(1);
        storeDTO.setManagerStaffId(1);
        storeDTO.setLastUpdate(LocalDateTime.of(2025, 6, 10, 12, 58));
    }

    // Tests for GET /api/customers/city/{city}/stores
    @Test
    public void testGetStoresByCity_Success() throws Exception {
        // Arrange
        when(cityRepository.findByCity("Mumbai")).thenReturn(Optional.of(city));
        when(storeRepository.findByAddressCity(city)).thenReturn(Arrays.asList(store));
        when(storeMapper.toDto(store)).thenReturn(storeDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customers/city/Mumbai/stores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].storeId").value(1))
                .andExpect(jsonPath("$[0].addressId").value(1))
                .andExpect(jsonPath("$[0].managerStaffId").value(1));
    }

    @Test
    public void testGetStoresByCity_NoStoresFound() throws Exception {
        // Arrange
        when(cityRepository.findByCity("Mumbai")).thenReturn(Optional.of(city));
        when(storeRepository.findByAddressCity(city)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/customers/city/Mumbai/stores")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStaffByCity_Success() throws Exception {
        // Arrange
        when(cityRepository.findByCity("Mumbai")).thenReturn(Optional.of(city));
        when(staffRepository.findByAddressCity(city)).thenReturn(Arrays.asList(staff));
        when(staffMapper.staffToDTO(staff)).thenReturn(staffDTO);

        // Act & Assert
        mockMvc.perform(get("/api/customers/city/Mumbai/staff")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].staffId").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Priya"))
                .andExpect(jsonPath("$[0].lastName").value("Sharma"));
    }

    @Test
    public void testGetStaffByCity_NoStaffFound() throws Exception {
        // Arrange
        when(cityRepository.findByCity("Mumbai")).thenReturn(Optional.of(city));
        when(staffRepository.findByAddressCity(city)).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/customers/city/Mumbai/staff")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

