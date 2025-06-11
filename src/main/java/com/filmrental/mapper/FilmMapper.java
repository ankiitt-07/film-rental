package com.filmrental.mapper;

import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Film;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class FilmMapper {

    public FilmDTO toDto(Film film) {
        if (film == null) {
            return null;
        }
        FilmDTO dto = new FilmDTO();
        dto.setFilmId(film.getFilmId());
        dto.setTitle(film.getTitle());
        dto.setDescription(film.getDescription());
        dto.setReleaseYear(film.getReleaseYear());
        dto.setLanguageId(film.getLanguage() != null ? film.getLanguage().getLanguageId() : null);
        dto.setOriginalLanguageId(film.getOriginalLanguage() != null ? film.getOriginalLanguage().getLanguageId() : null);
        dto.setRentalDuration(film.getRentalDuration());
        dto.setRentalRate(film.getRentalRate());
        dto.setLength(film.getLength());
        dto.setReplacementCost(film.getReplacementCost());
        dto.setRating(film.getRating());
        dto.setSpecialFeatures(film.getSpecialFeatures());
        dto.setActorIds(film.getActors().stream()
                .map(actor -> actor.getActorId())
                .collect(Collectors.toList()));
        dto.setCategoryIds(film.getFilmCategories().stream()
                .map(fc -> fc.getCategory().getCategoryId())
                .collect(Collectors.toList()));
        return dto;
    }

    public Film toEntity(FilmDTO dto) {
        if (dto == null) {
            return null;
        }
        Film film = new Film();
        film.setFilmId(dto.getFilmId());
        film.setTitle(dto.getTitle());
        film.setDescription(dto.getDescription());
        film.setReleaseYear(dto.getReleaseYear());
        film.setRentalDuration(dto.getRentalDuration());
        film.setRentalRate(dto.getRentalRate());
        film.setLength(dto.getLength());
        film.setReplacementCost(dto.getReplacementCost());
        film.setRating(dto.getRating());
        film.setSpecialFeatures(dto.getSpecialFeatures());
        return film;
    }
}