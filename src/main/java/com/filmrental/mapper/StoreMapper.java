package com.filmrental.mapper;

import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {

    public StoreDTO toDto(Store store) {
        if (store == null) {
            return null;
        }
        StoreDTO dto = new StoreDTO();
        dto.setStoreId(store.getStoreId());
        dto.setFirstName(store.getManagerStaff() != null ? store.getManagerStaff().getFirstName() : null);
        dto.setLastName(store.getManagerStaff() != null ? store.getManagerStaff().getLastName() : null);
        dto.setEmail(store.getManagerStaff() != null ? store.getManagerStaff().getEmail() : null);
        dto.setAddressId(store.getAddress() != null ? store.getAddress().getAddressId() : null);
        return dto;
    }

    public Store toEntity(StoreDTO dto) {
        if (dto == null) {
            return null;
        }
        Store store = new Store();
        // Fields are set in the controller for creation
        return store;
    }
}