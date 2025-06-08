package com.filmrental.controller;

import com.filmrental.model.dto.*;
import com.filmrental.model.entity.Category;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.FilmCategory;
import com.filmrental.mapper.CategoryMapper;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.repository.CategoryRepository;
import com.filmrental.repository.FilmCategoryRepository;
import com.filmrental.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private FilmMapper filmMapper;

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FilmDTO>> getFilmsByCategory(@PathVariable String category) {
        List<Film> films = categoryRepository.findFilmsByCategoryName(category);
        if (films.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<FilmDTO> filmDTOs = films.stream()
                .map(filmMapper::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(filmDTOs, HttpStatus.OK);
    }

    @PutMapping("/update/category/{id}")
    public ResponseEntity<FilmDTO> updateFilmCategory(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        return filmRepository.findById(id).map(film -> {
            Category category = categoryRepository.findById(categoryDTO.category_id())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            // Check if a FilmCategory record exists
            FilmCategory filmCategory = filmCategoryRepository
                    .findByFilm_FilmIdAndCategory_CategoryId(film.getFilm_id(), category.getCategory_id())
                    .orElse(new FilmCategory());
            filmCategory.setFilm(film);
            filmCategory.setCategory(category);
            filmCategory.setLast_update(LocalDateTime.now());
            filmCategoryRepository.save(filmCategory);
            return new ResponseEntity<>(filmMapper.toDTO(film), HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}