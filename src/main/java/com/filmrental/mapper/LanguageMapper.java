package com.filmrental.mapper;

import com.filmrental.model.dto.LanguageDTO;
import com.filmrental.model.entity.Language;

public class LanguageMapper {

    public static LanguageDTO toDto(Language entity) {
        if (entity == null) return null;

        return new LanguageDTO(
                entity.getLanguageId(),
                entity.getName(),
                entity.getLastUpdate()
        );
    }

    public static Language toEntity(LanguageDTO dto) {
        if (dto == null) return null;

        Language entity = new Language();
        entity.setLanguageId(dto.languageId());
        entity.setName(dto.name());
        entity.setLastUpdate(dto.lastUpdate());
        return entity;
    }
}
