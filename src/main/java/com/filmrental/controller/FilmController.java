package com.filmrental.controller;

import com.filmrental.mapper.ActorMapper;
import com.filmrental.mapper.CategoryMapper;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.dto.CategoryDTO;
import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.*;
import com.filmrental.repository.ActorRepository;
import com.filmrental.repository.CategoryRepository;
import com.filmrental.repository.FilmRepository;
import com.filmrental.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ActorMapper actorMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<FilmDTO>> getAllFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Film> films = filmRepository.findAll(pageable);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.map(filmMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @PostMapping("/post")
//    public ResponseEntity<String> addFilm(@RequestBody FilmDTO filmDTO) {
//        try {
//            if (filmDTO.getTitle() == null || filmDTO.getLanguageId() == null) {
//                throw new IllegalArgumentException("Title and language ID are required");
//            }
//            if (filmRepository.findByTitle(filmDTO.getTitle()).isPresent()) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("Film already exists");
//            }
//            Language language = languageRepository.findById(filmDTO.getLanguageId())
//                    .orElseThrow(() -> new IllegalArgumentException("Language not found with ID: " + filmDTO.getLanguageId()));
//            Film film = filmMapper.toEntity(filmDTO);
//            film.setLanguage(language);
//            film.setLastUpdate(LocalDateTime.now());
//            filmRepository.save(film);
//            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating film");
//        }
//    }
    @PostMapping
public ResponseEntity<String> addFilm(@RequestBody FilmDTO filmDTO) {
    try {
        // Validate required fields
        if (filmDTO.getTitle() == null || filmDTO.getLanguageId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Title and language ID are required");
        }

        // Check if film already exists
        if (filmRepository.findByTitle(filmDTO.getTitle()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Film with title '" + filmDTO.getTitle() + "' already exists");
        }

        // Fetch language
        Language language = languageRepository.findById(filmDTO.getLanguageId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Language not found with ID: " + filmDTO.getLanguageId()));

        // Create Film entity
        Film film = new Film();
        film.setTitle(filmDTO.getTitle());
        film.setDescription(filmDTO.getDescription());
        film.setReleaseYear(filmDTO.getReleaseYear());
        film.setLanguage(language);
        film.setOriginalLanguage(filmDTO.getOriginalLanguageId() != null
                ? languageRepository.findById(filmDTO.getOriginalLanguageId()).orElse(null)
                : null);
        film.setRentalDuration(filmDTO.getRentalDuration());
        film.setRentalRate(filmDTO.getRentalRate());
        film.setLength(filmDTO.getLength());
        film.setReplacementCost(filmDTO.getReplacementCost());
        film.setRating(filmDTO.getRating());
        film.setSpecialFeatures(filmDTO.getSpecialFeatures());
        film.setLastUpdate(LocalDateTime.now());

        // Handle actors
        Set<Actor> actors = new HashSet<>();
        if (filmDTO.getActorIds() != null && !filmDTO.getActorIds().isEmpty()) {
            List<Actor> foundActors = actorRepository.findAllById(filmDTO.getActorIds());
            if (foundActors.size() != filmDTO.getActorIds().size()) {
                List<Integer> missingIds = filmDTO.getActorIds().stream()
                        .filter(id -> foundActors.stream().noneMatch(a -> a.getActorId().equals(id)))
                        .collect(Collectors.toList());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Actors not found for IDs: " + missingIds);
            }
            actors.addAll(foundActors);
        }
        film.setActors(actors);

        // Save the film
        filmRepository.save(film);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Film created successfully with ID: " + film.getFilmId());

    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error creating film: " + e.getMessage());
    }
}


    @GetMapping("/title/{title}")
    public ResponseEntity<List<FilmDTO>> getFilmsByTitle(@PathVariable String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
            List<Film> films = filmRepository.findByTitleContainingIgnoreCase(title);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<FilmDTO>> getFilmsByYear(@PathVariable Integer year) {
        try {
            if (year <= 0) {
                throw new IllegalArgumentException("Invalid year");
            }
            List<Film> films = filmRepository.findByReleaseYear(Year.of(year));
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/countbyyear")
    public ResponseEntity<List<Object[]>> getFilmCountByYear() {
        try {
            List<Object[]> results = filmRepository.findFilmCountByYear();
            return results.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/rentalduration/greaterthan/{duration}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalDurationGreaterThan(@PathVariable Integer duration) {
        try {
            if (duration <= 0) {
                throw new IllegalArgumentException("Invalid rental duration");
            }
            List<Film> films = filmRepository.findByRentalDurationGreaterThan(duration);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/rentalduration/lessthan/{duration}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalDurationLessThan(@PathVariable Integer duration) {
        try {
            if (duration <= 0) {
                throw new IllegalArgumentException("Invalid rental duration");
            }
            List<Film> films = filmRepository.findByRentalDurationLessThan(duration);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/rentalrate/greaterthan/{rate}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalRateGreaterThan(@PathVariable BigDecimal rate) {
        try {
            if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Invalid rental rate");
            }
            List<Film> films = filmRepository.findByRentalRateGreaterThan(rate);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/rentalrate/lessthan/{rate}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalRateLessThan(@PathVariable BigDecimal rate) {
        try {
            if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Invalid rental rate");
            }
            List<Film> films = filmRepository.findByRentalRateLessThan(rate);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/length/greaterthan/{length}")
    public ResponseEntity<List<FilmDTO>> getFilmsByLengthGreaterThan(@PathVariable Integer length) {
        try {
            if (length <= 0) {
                throw new IllegalArgumentException("Invalid length");
            }
            List<Film> films = filmRepository.findByLengthGreaterThan(length);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/length/lessthan/{length}")
    public ResponseEntity<List<FilmDTO>> getFilmsByLengthLessThan(@PathVariable Integer length) {
        try {
            if (length <= 0) {
                throw new IllegalArgumentException("Invalid length");
            }
            List<Film> films = filmRepository.findByLengthLessThan(length);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/year/between/{startYear}/{endYear}")
    public ResponseEntity<List<FilmDTO>> getFilmsByYearBetween(@PathVariable Integer startYear, @PathVariable Integer endYear) {
        try {
            if (startYear <= 0 || endYear <= 0 || startYear > endYear) {
                throw new IllegalArgumentException("Invalid year range");
            }
            List<Film> films = filmRepository.findByReleaseYearBetween(Year.of(startYear), Year.of(endYear));
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/rating/lessthan/{rating}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRatingLessThan(@PathVariable String rating) {
        try {
            if (rating == null || rating.trim().isEmpty()) {
                throw new IllegalArgumentException("Rating cannot be empty");
            }
            List<Film> films = filmRepository.findByRatingLessThan(rating);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/rating/greaterthan/{rating}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRatingGreaterThan(@PathVariable String rating) {
        try {
            if (rating == null || rating.trim().isEmpty()) {
                throw new IllegalArgumentException("Rating cannot be empty");
            }
            List<Film> films = filmRepository.findByRatingGreaterThan(rating);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/language/{name}")
    public ResponseEntity<List<FilmDTO>> getFilmsByLanguage(@PathVariable String name) {
        try {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Language name cannot be empty");
            }
            List<Film> films = filmRepository.findByLanguage_Name(name);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/category/{categoryName}")
    public ResponseEntity<List<FilmDTO>> getFilmsByCategory(@PathVariable String categoryName) {
        try {
            if (categoryName == null || categoryName.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty");
            }
            List<Film> films = filmRepository.findByFilmCategories_Category_Name(categoryName);
            return films.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(films.stream().map(filmMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/actors")
    public ResponseEntity<List<ActorDTO>> getActorsByFilmId(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid film ID");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            List<ActorDTO> actorDTOs = film.getActors().stream()
                    .map(actorMapper::toDto)
                    .collect(Collectors.toList());
            return actorDTOs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(actorDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/title/{id}")
    public ResponseEntity<FilmDTO> updateTitle(@PathVariable Integer id, @RequestBody String title) {
        try {
            if (id <= 0 || title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid ID or title");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            film.setTitle(title);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/releaseyear/{id}")
    public ResponseEntity<FilmDTO> updateReleaseYear(@PathVariable Integer id, @RequestBody Integer year) {
        try {
            if (id <= 0 || year == null || year <= 0) {
                throw new IllegalArgumentException("Invalid ID or year");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            film.setReleaseYear(Year.of(year));
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/rentalduration/{id}")
    public ResponseEntity<FilmDTO> updateRentalDuration(@PathVariable Integer id, @RequestBody Integer rentalDuration) {
        try {
            if (id <= 0 || rentalDuration == null || rentalDuration <= 0) {
                throw new IllegalArgumentException("Invalid ID or rental duration");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            film.setRentalDuration(rentalDuration);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/rentalrate/{id}")
    public ResponseEntity<FilmDTO> updateRentalRate(@PathVariable Integer id, @RequestBody BigDecimal rentalRate) {
        try {
            if (id <= 0 || rentalRate == null || rentalRate.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Invalid ID or rental rate");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            film.setRentalRate(rentalRate);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/rating/{id}")
    public ResponseEntity<FilmDTO> updateRating(@PathVariable Integer id, @RequestBody String rating) {
        try {
            if (id <= 0 || rating == null || rating.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid ID or rating");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            film.setRating(rating);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/language/{id}")
    public ResponseEntity<FilmDTO> updateLanguage(@PathVariable Integer id, @RequestBody Integer languageId) {
        try {
            if (id <= 0 || languageId == null || languageId <= 0) {
                throw new IllegalArgumentException("Invalid film ID or language ID");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
            Language language = languageRepository.findById(languageId)
                    .orElseThrow(() -> new IllegalArgumentException("Language not found with ID: " + languageId));
            film.setLanguage(language);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

//    @PutMapping("/update/{id}/category")
//    public ResponseEntity<List<CategoryDTO>> assignCategory(@PathVariable Integer id, @RequestBody Integer categoryId) {
//        try {
//            if (id <= 0 || categoryId <= 0) {
//                throw new IllegalArgumentException("Invalid film or category ID");
//            }
//            Film film = filmRepository.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + id));
//            Category category = categoryRepository.findById(categoryId)
//                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + categoryId));
//            FilmCategory filmCategory = new FilmCategory();
//            filmCategory.setId(new FilmCategoryId(film.getFilmId(), category.getCategoryId()));
//            filmCategory.setFilm(film);
//            filmCategory.setCategory(category);
//            filmCategory.setLastUpdate(LocalDateTime.now());
//            film.getFilmCategories().add(filmCategory);
//            film.setLastUpdate(LocalDateTime.now());
//            Film updatedFilm = filmRepository.save(film);
//            List<CategoryDTO> categoryDTOs = updatedFilm.getFilmCategories().stream()
//                    .map(fc -> categoryMapper.toDto(fc.getCategory()))
//                    .collect(Collectors.toList());
//            return ResponseEntity.ok(categoryDTOs);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
