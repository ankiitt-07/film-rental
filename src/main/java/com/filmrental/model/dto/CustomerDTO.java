package com.filmrental.model.dto;

import java.time.LocalDateTime;

public record CustomerDTO(
        Integer customerId,
        Integer storeId,
        String firstName,
        String lastName,
        String email,
        Integer addressId,
        Boolean active,
        LocalDateTime createDate,
        LocalDateTime lastUpdate
) {}