package com.filmrental.model.dto;

import java.time.LocalDateTime;

public record LanguageDTO(
        Integer languageId,
        String name,
        LocalDateTime lastUpdate
) {}
