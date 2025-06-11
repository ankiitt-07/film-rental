package com.filmrental.repository;

import com.filmrental.model.entity.FilmCategory;
import com.filmrental.model.entity.FilmCategoryId;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmCategoryRepository extends JpaRepository<FilmCategory, FilmCategoryId> {
    List<FilmCategory> findByFilm_FilmId(Integer filmId);
    List<FilmCategory> findByCategory_CategoryId(Integer categoryId);
    Optional<FilmCategory> findByFilm_FilmIdAndCategory_CategoryId(Integer filmId, Integer categoryId);
}