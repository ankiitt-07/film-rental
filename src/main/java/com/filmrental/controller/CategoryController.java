package com.filmrental.controller;

import com.filmrental.model.dto.CategoryDTO;
import com.filmrental.model.dto.FilmDTO;
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
@RequestMapping("/api/categories")
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

    // Get all categories
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            List<CategoryDTO> categories = categoryRepository.findAll()
                    .stream()
                    .map(categoryMapper::toDto)
                    .collect(Collectors.toList());
            if (categories.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching all categories", e);
        }
    }

    // Get category by ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid category ID: ID must be a positive integer");
            }
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
            return ResponseEntity.ok(categoryMapper.toDto(category));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching category with ID: " + id, e);
        }
    }

    // Get films by category name
    @GetMapping("/films/{category}")
    public ResponseEntity<List<FilmDTO>> getFilmsByCategory(@PathVariable String category) {
        try {
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid category: category name cannot be empty");
            }
            Category categoryEntity = categoryRepository.findByName(category)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found: " + category));
            List<Film> films = categoryEntity.getFilmCategories()
                    .stream()
                    .map(FilmCategory::getFilm)
                    .collect(Collectors.toList());
            if (films.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error fetching films for category: " + category, e);
        }
    }

    // Create a new category
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        try {
            if (categoryDTO == null || categoryDTO.name() == null || categoryDTO.name().trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid category: category name cannot be empty");
            }
            Category existingCategory = categoryRepository.findByName(categoryDTO.name())
                    .orElse(null);
            if (existingCategory != null) {
                throw new IllegalArgumentException("Category already exists: " + categoryDTO.name());
            }
            Category category = categoryMapper.toEntity(categoryDTO);
            category.setLastUpdate(LocalDateTime.now());
            Category savedCategory = categoryRepository.save(category);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryMapper.toDto(savedCategory));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating category: " + categoryDTO.name(), e);
        }
    }

    // Update an existing category
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid category ID: ID must be a positive integer");
            }
            if (categoryDTO == null || categoryDTO.name() == null || categoryDTO.name().trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid category: category name cannot be empty");
            }
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
            category.setName(categoryDTO.name());
            category.setLastUpdate(LocalDateTime.now());
            Category updatedCategory = categoryRepository.save(category);
            return ResponseEntity.ok(categoryMapper.toDto(updatedCategory));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating category with ID: " + id, e);
        }
    }

    // Update the category of a film
    @PutMapping("/films/{id}")
    public ResponseEntity<FilmDTO> updateFilmCategory(@PathVariable Integer id, @RequestBody CategoryDTO categoryDTO) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid film ID: ID must be a positive integer");
            }
            if (categoryDTO == null || categoryDTO.category_id() == null || categoryDTO.category_id() <= 0) {
                throw new IllegalArgumentException("Invalid category: category ID must be a positive integer");
            }
            return filmRepository.findById(id).map(film -> {
                Category category = categoryRepository.findById(categoryDTO.category_id())
                        .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryDTO.category_id()));
                FilmCategory filmCategory = filmCategoryRepository
                        .findByFilm_FilmIdAndCategory_CategoryId(film.getFilmId(), category.getCategoryId())
                        .orElse(new FilmCategory());
                filmCategory.setFilm(film);
                filmCategory.setCategory(category);
                filmCategory.setLastUpdate(LocalDateTime.now());
                filmCategoryRepository.save(filmCategory);
                return ResponseEntity.ok(filmMapper.toDto(film));
            }).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error updating category for film with ID: " + id, e);
        }
    }

    // Delete a category by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid category ID: ID must be a positive integer");
            }
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + id));
            categoryRepository.delete(category);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting category with ID: " + id, e);
        }
    }
}