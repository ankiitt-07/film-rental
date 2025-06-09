package com.filmrental.mapper;

import com.filmrental.model.dto.FilmCategoryDTO;
import com.filmrental.model.entity.FilmCategory;
import com.filmrental.model.entity.FilmCategoryId;
import org.springframework.stereotype.Component;

@Component
public class FilmCategoryMapper {

    public static FilmCategoryDTO toDto(FilmCategory filmCategory) {
        if (filmCategory == null) return null;

        return new FilmCategoryDTO(
                filmCategory.getId().getFilmId(),
                filmCategory.getId().getCategoryId(),
                filmCategory.getLastUpdate()
        );
    }

    public static FilmCategory toEntity(FilmCategoryDTO dto) {
        if (dto == null) return null;

        FilmCategory filmCategory = new FilmCategory();
        filmCategory.setId(new FilmCategoryId(dto.film_id(), dto.category_id()));
        filmCategory.setLastUpdate(dto.last_update());
        return filmCategory;
    }
}