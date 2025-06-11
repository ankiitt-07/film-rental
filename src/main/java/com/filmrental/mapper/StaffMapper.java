package com.filmrental.mapper;

import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.entity.Staff;
import org.springframework.stereotype.Component;

@Component
public class StaffMapper {

    public StaffDTO toDto(Staff staff) {
        if (staff == null) {
            return null;
        }
        StaffDTO dto = new StaffDTO();
        dto.setStaffId(staff.getStaffId());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setEmail(staff.getEmail());
        dto.setActive(staff.getActive());
        dto.setPicture(staff.getPicture());
        dto.setUsername(staff.getUsername());
        dto.setPassword(staff.getPassword());
        dto.setStoreId(staff.getStore() != null ? staff.getStore().getStoreId() : null);
        dto.setAddressId(staff.getAddress() != null ? staff.getAddress().getAddressId() : null);
        return dto;
    }

    public Staff toEntity(StaffDTO dto) {
        if (dto == null) {
            return null;
        }
        Staff staff = new Staff();
        staff.setStaffId(dto.getStaffId());
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setEmail(dto.getEmail());
        staff.setActive(dto.getActive());
        staff.setPicture(dto.getPicture());
        staff.setUsername(dto.getUsername());
        staff.setPassword(dto.getPassword());
        return staff;
    }
}