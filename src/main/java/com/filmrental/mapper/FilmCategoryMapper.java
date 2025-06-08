package com.filmrental.mapper;

import com.filmrental.model.dto.FilmCategoryDTO;
import com.filmrental.model.entity.FilmCategory;
import com.filmrental.model.entity.FilmCategoryId;

public class FilmCategoryMapper {
    public static FilmCategoryDTO toDto(FilmCategory filmCategory) {
        if (filmCategory == null) return null;
        return new FilmCategoryDTO(
                filmCategory.getId().getFilm_id(),
                filmCategory.getId().getCategory_id(),
                filmCategory.getLast_update()
        );
    }

    public static FilmCategory toEntity(FilmCategoryDTO dto) {
        if (dto == null) return null;
        FilmCategory filmCategory = new FilmCategory();
        FilmCategoryId id = new FilmCategoryId();
        id.setFilm_id(dto.film_id());
        id.setCategory_id(dto.category_id());
        filmCategory.setId(id);
        filmCategory.setLast_update(dto.last_update());
        return filmCategory;
    }
}