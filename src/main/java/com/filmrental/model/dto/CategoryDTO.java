package com.filmrental.model.dto;

import java.time.LocalDateTime;

public record CategoryDTO(
        Integer category_id,
        String name,
        LocalDateTime last_update
) {}