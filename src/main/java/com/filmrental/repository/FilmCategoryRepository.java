package com.filmrental.repository;

import com.filmrental.model.entity.FilmCategory;
import com.filmrental.model.entity.FilmCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId> {
    List<FilmCategory> findByFilm_FilmId(Integer filmId);
    List<FilmCategory> findByCategory_CategoryId(Integer categoryId);
    Optional<FilmCategory> findByFilm_FilmIdAndCategory_CategoryId(Integer filmId, Integer categoryId);
}