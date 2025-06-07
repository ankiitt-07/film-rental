package com.filmrental.mapper;

import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.Store;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;




@Mapper(componentModel = "spring")
public interface StoreMapper {
    StoreMapper INSTANCE = Mappers.getMapper(StoreMapper.class);

    @Mapping(source = "managerStaff.staffId", target = "managerStaffId")
    @Mapping(source = "address.addressId", target = "addressId")
    StoreDTO toDto(Store entity);

    @Mapping(source = "managerStaffId", target = "managerStaff.staffId")
    @Mapping(source = "addressId", target = "address.addressId")
    Store toEntity(StoreDTO dto);
}