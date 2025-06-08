package com.filmrental.model.dto;

import java.time.LocalDateTime;

public record FilmCategoryDTO(
        Integer film_id,
        Integer category_id,
        LocalDateTime last_update
) {}