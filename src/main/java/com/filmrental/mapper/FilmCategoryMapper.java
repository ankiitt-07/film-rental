package com.filmrental.mapper;

import com.filmrental.model.dto.FilmCategoryDTO;
import com.filmrental.model.entity.FilmCategory;
import com.filmrental.model.entity.FilmCategoryId;
import org.springframework.stereotype.Component;

@Component
public class FilmCategoryMapper {

    public FilmCategoryDTO toDto(FilmCategory filmCategory) {
        if (filmCategory == null) {
            return null;
        }
        FilmCategoryDTO dto = new FilmCategoryDTO();
        dto.setFilmId(filmCategory.getId().getFilmId());
        dto.setCategoryId(filmCategory.getId().getCategoryId());
        return dto;
    }

    public FilmCategory toEntity(FilmCategoryDTO dto) {
        if (dto == null) {
            return null;
        }
        FilmCategory filmCategory = new FilmCategory();
        filmCategory.setId(new FilmCategoryId(dto.getFilmId(), dto.getCategoryId()));
        return filmCategory;
    }
}