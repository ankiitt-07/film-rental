package com.filmrental.repository;

import com.filmrental.model.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    Optional<Film> findByTitle(String title);
    List<Film> findByLanguage_LanguageId(Integer languageId);
    List<Film> findByReleaseYear(int releaseYear);
}