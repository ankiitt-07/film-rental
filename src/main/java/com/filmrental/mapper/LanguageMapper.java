package com.filmrental.mapper;

import com.filmrental.model.dto.LanguageDTO;
import com.filmrental.model.entity.Language;

public class LanguageMapper {

    public static LanguageDTO toDto(Language language) {
        if (language == null) return null;

        return new LanguageDTO(
                language.getLanguageId(),
                language.getName(),
                language.getLastUpdate()
        );
    }

    public static Language toEntity(LanguageDTO dto) {
        if (dto == null) return null;

        Language language = new Language();
        language.setLanguageId(dto.languageId());
        language.setName(dto.name());
        language.setLastUpdate(dto.lastUpdate());
        return language;
    }
}