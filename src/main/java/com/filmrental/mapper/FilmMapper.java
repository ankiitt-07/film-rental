package com.filmrental.mapper;

import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FilmMapper {

    @Autowired
    private ActorMapper actorMapper;

    public FilmDTO toDto(Film film) {
        if (film == null) return null;

        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setFilmId(film.getFilmId());
        filmDTO.setTitle(film.getTitle());
        filmDTO.setDescription(film.getDescription());
        filmDTO.setReleaseYear(film.getReleaseYear());
        filmDTO.setLanguageId(film.getLanguage() != null ? film.getLanguage().getLanguageId() : null);
        filmDTO.setOriginalLanguageId(film.getOriginalLanguage() != null ? film.getOriginalLanguage().getLanguageId() : null);
        filmDTO.setRentalDuration(film.getRentalDuration());
        filmDTO.setRentalRate(film.getRentalRate());
        filmDTO.setLength(film.getLength());
        filmDTO.setReplacementCost(film.getReplacementCost());
        filmDTO.setRating(film.getRating());
        filmDTO.setSpecialFeatures(film.getSpecialFeatures());
        filmDTO.setLastUpdate(film.getLastUpdate());
        if (film.getActors() != null) {
            filmDTO.setActors(film.getActors().stream()
                    .map(actorMapper::toActorDto)
                    .collect(Collectors.toList()));
        }
        return filmDTO;
    }

    public Film toEntity(FilmDTO dto) {
        if (dto == null) return null;

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
        film.setLastUpdate(dto.getLastUpdate());
        return film;
    }
}