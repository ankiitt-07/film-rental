package com.filmrental.mapper;

import com.filmrental.model.dto.StoreDTO;
import com.filmrental.model.entity.Store;

public class StoreMapper {

    public static StoreDTO toDto(Store store) {
        if (store == null) return null;

        return new StoreDTO(
                store.getStoreId(),
                store.getManagerStaff() != null ? store.getManagerStaff().getStaffId() : null,
                store.getAddress() != null ? store.getAddress().getAddressId() : null,
                store.getLastUpdate()
        );
    }

    public static Store toEntity(StoreDTO dto) {
        if (dto == null) return null;

        Store store = new Store();
        store.setStoreId(dto.getStoreId());
        store.setLastUpdate(dto.getLastUpdate());
        return store;
    }
}