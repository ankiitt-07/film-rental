package com.filmrental.repository;

import com.filmrental.model.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Short> {
}
