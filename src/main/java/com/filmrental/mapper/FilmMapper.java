package com.filmrental.mapper;

import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Film;
import org.springframework.stereotype.Component;

@Component
public class FilmMapper {

    public FilmDTO toDto(Film film) {
        if (film == null) return null;

        return new FilmDTO(
                film.getFilmId(),
                film.getTitle(),
                film.getDescription(),
                film.getReleaseYear(),
                film.getLanguage() != null ? film.getLanguage().getLanguageId() : null,
                film.getOriginalLanguage() != null ? film.getOriginalLanguage().getLanguageId() : null,
                film.getRentalDuration(),
                film.getRentalRate(),
                film.getLength(),
                film.getReplacementCost(),
                film.getRating(),
                film.getSpecialFeatures(),
                film.getLastUpdate()
        );
    }

    public Film toEntity(FilmDTO dto) {
        if (dto == null) return null;

        Film film = new Film();
        film.setFilmId(dto.filmId());
        film.setTitle(dto.title());
        film.setDescription(dto.description());
        film.setReleaseYear(dto.releaseYear());
        film.setRentalDuration(dto.rentalDuration());
        film.setRentalRate(dto.rentalRate());
        film.setLength(dto.length());
        film.setReplacementCost(dto.replacementCost());
        film.setRating(dto.rating());
        film.setSpecialFeatures(dto.specialFeatures());
        film.setLastUpdate(dto.lastUpdate());
        return film;
    }
}