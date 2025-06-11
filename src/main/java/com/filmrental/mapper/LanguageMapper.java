package com.filmrental.mapper;

import com.filmrental.model.dto.LanguageDTO;
import com.filmrental.model.entity.Language;
import org.springframework.stereotype.Component;

@Component
public class LanguageMapper {

    public LanguageDTO toDto(Language language) {
        if (language == null) {
            return null;
        }
        LanguageDTO dto = new LanguageDTO();
        dto.setLanguageId(language.getLanguageId());
        dto.setName(language.getName());
        return dto;
    }

    public Language toEntity(LanguageDTO dto) {
        if (dto == null) {
            return null;
        }
        Language language = new Language();
        language.setLanguageId(dto.getLanguageId());
        language.setName(dto.getName());
        return language;
    }
}