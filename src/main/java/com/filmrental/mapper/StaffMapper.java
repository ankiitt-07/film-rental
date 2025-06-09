package com.filmrental.mapper;

import com.filmrental.model.dto.StaffDTO;
import com.filmrental.model.entity.Staff;

public class StaffMapper {

    public static StaffDTO staffToDTO(Staff staff) {
        if (staff == null) return null;
        StaffDTO dto = new StaffDTO();
        dto.setStaffId(staff.getStaffId());
        dto.setFirstName(staff.getFirstName());
        dto.setLastName(staff.getLastName());
        dto.setEmail(staff.getEmail());
        dto.setPhone(staff.getPhone());
        dto.setActive(staff.getActive());
        dto.setPicture(staff.getPicture());
        dto.setStoreId(staff.getStore() != null ? staff.getStore().getStoreId() : null);
        dto.setAddressId(staff.getAddress() != null ? staff.getAddress().getAddressId() : null);
        return dto;
    }

    public static Staff DTOToStaff(StaffDTO dto) {
        if (dto == null) return null;
        Staff staff = new Staff();
        staff.setStaffId(dto.getStaffId());
        staff.setFirstName(dto.getFirstName());
        staff.setLastName(dto.getLastName());
        staff.setEmail(dto.getEmail());
        staff.setPhone(dto.getPhone());
        staff.setActive(dto.getActive());
        staff.setPicture(dto.getPicture());
        return staff;
    }
}