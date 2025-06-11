package com.filmrental.controller;

import com.filmrental.exception.ResourceNotFoundException;
import com.filmrental.mapper.ActorMapper;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Actor;
import com.filmrental.model.entity.Category;
import com.filmrental.model.entity.Film;
import com.filmrental.model.entity.FilmCategory;
import com.filmrental.model.entity.Language;
import com.filmrental.repository.ActorRepository;
import com.filmrental.repository.CategoryRepository;
import com.filmrental.repository.FilmCategoryRepository;
import com.filmrental.repository.FilmRepository;
import com.filmrental.repository.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private FilmCategoryRepository filmCategoryRepository;

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private ActorMapper actorMapper;

    @PostMapping("/post")
    public ResponseEntity<FilmDTO> addFilm(@RequestBody FilmDTO filmDTO) {
        try {
            if (filmDTO == null) {
                throw new IllegalArgumentException("Film data cannot be null");
            }
            if (filmDTO.getTitle() == null || filmDTO.getTitle().trim().isEmpty()) {
                throw new IllegalArgumentException("Film title cannot be empty");
            }
            if (filmDTO.getLanguageId() == null) {
                throw new IllegalArgumentException("Language ID is required");
            }

            // Create and save Film entity
            Film film = new Film();
            film.setTitle(filmDTO.getTitle());
            film.setDescription(filmDTO.getDescription());
            film.setReleaseYear(filmDTO.getReleaseYear());
            film.setRentalDuration(filmDTO.getRentalDuration());
            film.setRentalRate(filmDTO.getRentalRate());
            film.setLength(filmDTO.getLength());
            film.setReplacementCost(filmDTO.getReplacementCost());
            film.setRating(filmDTO.getRating());
            film.setSpecialFeatures(filmDTO.getSpecialFeatures());
            film.setLastUpdate(LocalDateTime.now());

            // Set language
            Language language = languageRepository.findById(filmDTO.getLanguageId())
                    .orElseThrow(() -> new ResourceNotFoundException("Language not found for ID: " + filmDTO.getLanguageId()));
            film.setLanguage(language);

            // Set original language if provided
            if (filmDTO.getOriginalLanguageId() != null) {
                Language originalLanguage = languageRepository.findById(filmDTO.getOriginalLanguageId())
                        .orElseThrow(() -> new ResourceNotFoundException("Original language not found for ID: " + filmDTO.getOriginalLanguageId()));
                film.setOriginalLanguage(originalLanguage);
            }

            Film savedFilm = filmRepository.save(film);

            // Handle actors
            if (filmDTO.getActors() != null && !filmDTO.getActors().isEmpty()) {
                Set<Actor> associatedActors = new HashSet<>();
                for (ActorDTO actorDTO : filmDTO.getActors()) {
                    if (actorDTO.getFirstName() == null || actorDTO.getLastName() == null) {
                        throw new IllegalArgumentException("Actor first name and last name are required");
                    }
                    Actor actor = actorRepository.findByFirstNameAndLastName(actorDTO.getFirstName(), actorDTO.getLastName())
                            .orElseGet(() -> {
                                Actor newActor = new Actor();
                                newActor.setFirstName(actorDTO.getFirstName());
                                newActor.setLastName(actorDTO.getLastName());
                                newActor.setLastUpdate(LocalDateTime.now());
                                return actorRepository.save(newActor);
                            });
                    actor.getFilms().add(savedFilm);
                    actorRepository.save(actor);
                    associatedActors.add(actor);
                }
                savedFilm.setActors(associatedActors);
            }

            // Return DTO
            return ResponseEntity.ok(filmMapper.toDto(savedFilm));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to add film: " + e.getMessage());
        }
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<FilmDTO> getFilmByTitle(@PathVariable String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
            Film film = filmRepository.findByTitle(title)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with title: " + title));
            return ResponseEntity.ok(filmMapper.toDto(film));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve film by title: " + e.getMessage());
        }
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<FilmDTO>> getFilmsByYear(@PathVariable int year) {
        try {
            if (year < 1888 || year > Year.now().getValue()) {
                throw new IllegalArgumentException("Invalid release year: " + year);
            }
            List<Film> films = filmRepository.findByReleaseYear(year);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found for year: " + year);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by year: " + e.getMessage());
        }
    }

    @GetMapping("/duration/gt/{rd}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalDurationGreaterThan(@PathVariable Integer rd) {
        try {
            if (rd == null || rd < 0) {
                throw new IllegalArgumentException("Rental duration must be non-negative");
            }
            List<Film> films = filmRepository.findByRentalDurationGreaterThan(rd);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with rental duration greater than: " + rd);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by rental duration: " + e.getMessage());
        }
    }

    @GetMapping("/duration/lt/{rd}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalDurationLessThan(@PathVariable Integer rd) {
        try {
            if (rd == null || rd < 0) {
                throw new IllegalArgumentException("Rental duration must be non-negative");
            }
            List<Film> films = filmRepository.findByRentalDurationLessThan(rd);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with rental duration less than: " + rd);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by rental duration: " + e.getMessage());
        }
    }

    @GetMapping("/rate/gt/{rate}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalRateGreaterThan(@PathVariable BigDecimal rate) {
        try {
            if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Rental rate must be non-negative");
            }
            List<Film> films = filmRepository.findByRentalRateGreaterThan(rate);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with rental rate greater than: " + rate);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by rental rate: " + e.getMessage());
        }
    }

    @GetMapping("/rate/lt/{rate}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRentalRateLessThan(@PathVariable BigDecimal rate) {
        try {
            if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Rental rate must be non-negative");
            }
            List<Film> films = filmRepository.findByRentalRateLessThan(rate);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with rental rate less than: " + rate);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by rental rate: " + e.getMessage());
        }
    }

    @GetMapping("/length/gt/{length}")
    public ResponseEntity<List<FilmDTO>> getFilmsByLengthGreaterThan(@PathVariable Integer length) {
        try {
            if (length == null || length < 0) {
                throw new IllegalArgumentException("Length must be non-negative");
            }
            List<Film> films = filmRepository.findByLengthGreaterThan(length);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with length greater than: " + length);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by length: " + e.getMessage());
        }
    }

    @GetMapping("/length/lt/{length}")
    public ResponseEntity<List<FilmDTO>> getFilmsByLengthLessThan(@PathVariable Integer length) {
        try {
            if (length == null || length < 0) {
                throw new IllegalArgumentException("Length must be non-negative");
            }
            List<Film> films = filmRepository.findByLengthLessThan(length);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with length less than: " + length);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by length: " + e.getMessage());
        }
    }

    @GetMapping("/betweenyear/{from}/{to}")
    public ResponseEntity<List<FilmDTO>> getFilmsBetweenYears(@PathVariable int from, @PathVariable int to) {
        try {
            if (from < 1888 || to > Year.now().getValue() || from > to) {
                throw new IllegalArgumentException("Invalid year range: from " + from + " to " + to);
            }
            List<Film> films = filmRepository.findByReleaseYearGreaterThanEqualAndReleaseYearLessThanEqual(from, to);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found between years " + from + " and " + to);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by year range: " + e.getMessage());
        }
    }

    @GetMapping("/rating/gt/{rating}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRatingGreaterThan(@PathVariable String rating) {
        try {
            if (rating == null || rating.trim().isEmpty()) {
                throw new IllegalArgumentException("Rating cannot be empty");
            }
            List<Film> films = filmRepository.findByRatingGreaterThan(rating);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with rating greater than: " + rating);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by rating: " + e.getMessage());
        }
    }

    @GetMapping("/rating/lt/{rating}")
    public ResponseEntity<List<FilmDTO>> getFilmsByRatingLessThan(@PathVariable String rating) {
        try {
            if (rating == null || rating.trim().isEmpty()) {
                throw new IllegalArgumentException("Rating cannot be empty");
            }
            List<Film> films = filmRepository.findByRatingLessThan(rating);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found with rating less than: " + rating);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by rating: " + e.getMessage());
        }
    }

    @GetMapping("/language/{lang}")
    public ResponseEntity<List<FilmDTO>> getFilmsByLanguage(@PathVariable Integer lang) {
        try {
            if (lang == null || lang <= 0) {
                throw new IllegalArgumentException("Invalid language ID");
            }
            List<Film> films = filmRepository.findByLanguageLanguageId(lang);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found for language ID: " + lang);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by language: " + e.getMessage());
        }
    }

    @GetMapping("/countbyyear")
    public ResponseEntity<Map<Integer, Long>> getFilmCountByYear() {
        try {
            Map<Integer, Long> countByYear = filmRepository.findAll().stream()
                    .collect(Collectors.groupingBy(
                            f -> f.getReleaseYear().getValue(),
                            Collectors.counting()
                    ));
            if (countByYear.isEmpty()) {
                throw new ResourceNotFoundException("No films found");
            }
            return ResponseEntity.ok(countByYear);
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve film count by year: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/actors")
    public ResponseEntity<List<ActorDTO>> getActorsByFilm(@PathVariable Integer id) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid film ID: ID must be a positive integer");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            List<ActorDTO> actorDTOs = film.getActors().stream()
                    .map(actorMapper::toActorDto)
                    .collect(Collectors.toList());
            if (actorDTOs.isEmpty()) {
                throw new ResourceNotFoundException("No actors found for film with ID: " + id);
            }
            return ResponseEntity.ok(actorDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve actors for film: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<FilmDTO>> getFilmsByCategory(@PathVariable String category) {
        try {
            if (category == null || category.trim().isEmpty()) {
                throw new IllegalArgumentException("Category name cannot be empty");
            }
            List<Film> films = filmRepository.findByFilmCategoriesCategoryName(category);
            if (films.isEmpty()) {
                throw new ResourceNotFoundException("No films found for category: " + category);
            }
            List<FilmDTO> filmDTOs = films.stream()
                    .map(filmMapper::toDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by category: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/actor")
    public ResponseEntity<FilmDTO> assignActorToFilm(@PathVariable Integer id, @RequestBody ActorDTO actorDTO) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid film ID: ID must be a positive integer");
            }
            if (actorDTO.getFirstName() == null || actorDTO.getLastName() == null) {
                throw new IllegalArgumentException("Actor first name and last name are required");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            Actor actor = actorRepository.findByFirstNameAndLastName(actorDTO.getFirstName(), actorDTO.getLastName())
                    .orElseGet(() -> {
                        Actor newActor = new Actor();
                        newActor.setFirstName(actorDTO.getFirstName());
                        newActor.setLastName(actorDTO.getLastName());
                        newActor.setLastUpdate(LocalDateTime.now());
                        return actorRepository.save(newActor);
                    });
            actor.getFilms().add(film);
            actorRepository.save(actor);
            film.getActors().add(actor);
            return ResponseEntity.ok(filmMapper.toDto(film));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign actor to film: " + e.getMessage());
        }
    }

    @PutMapping("/update/title/{id}")
    public ResponseEntity<FilmDTO> updateFilmTitle(@PathVariable Integer id, @RequestBody String title) {
        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            film.setTitle(title);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film title: " + e.getMessage());
        }
    }

    @PutMapping("/update/releaseyear/{id}")
    public ResponseEntity<FilmDTO> updateFilmReleaseYear(@PathVariable Integer id, @RequestBody Year releaseYear) {
        try {
            if (releaseYear == null || releaseYear.getValue() < 1888 || releaseYear.getValue() > Year.now().getValue()) {
                throw new IllegalArgumentException("Invalid release year");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            film.setReleaseYear(releaseYear);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film release year: " + e.getMessage());
        }
    }

    @PutMapping("/update/rentalduration/{id}")
    public ResponseEntity<FilmDTO> updateFilmRentalDuration(@PathVariable Integer id, @RequestBody Integer rentalDuration) {
        try {
            if (rentalDuration == null || rentalDuration < 0) {
                throw new IllegalArgumentException("Rental duration must be non-negative");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            film.setRentalDuration(rentalDuration);
            film.setLastUpdate(LocalDateTime.now());
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film rental duration: " + e.getMessage());
        }
    }

    @PutMapping("/update/rentalrate/{id}")
    public ResponseEntity<FilmDTO> updateFilmRentalRate(@PathVariable Integer id, @RequestBody BigDecimal rentalRate) {
        try {
            if (rentalRate == null || rentalRate.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Rental rate must be non-negative");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            film.setRentalRate(rentalRate);
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film rental rate: " + e.getMessage());
        }
    }

    @PutMapping("/update/rating/{id}")
    public ResponseEntity<FilmDTO> updateFilmRating(@PathVariable Integer id, @RequestBody String rating) {
        try {
            if (rating == null || rating.trim().isEmpty()) {
                throw new IllegalArgumentException("Rating cannot be empty");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            film.setRating(rating);
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film rating: " + e.getMessage());
        }
    }

    @PutMapping("/update/language/{id}")
    public ResponseEntity<FilmDTO> updateFilmLanguage(@PathVariable Integer id, @RequestBody Integer languageId) {
        try {
            throw new UnsupportedOperationException("Language update not implemented");
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film language: " + e.getMessage());
        }
    }

    @PutMapping("/update/category/{id}")
    public ResponseEntity<FilmDTO> updateFilmCategory(@PathVariable Integer id, @RequestBody Integer categoryId) {
        try {
            if (id == null || id <= 0) {
                throw new IllegalArgumentException("Invalid film ID: ID must be a positive integer");
            }
            if (categoryId == null || categoryId <= 0) {
                throw new IllegalArgumentException("Invalid category ID: ID must be a positive integer");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + categoryId));
            FilmCategory filmCategory = filmCategoryRepository
                    .findByFilm_FilmIdAndCategory_CategoryId(film.getFilmId(), category.getCategoryId())
                    .orElse(new FilmCategory());
            filmCategory.setFilm(film);
            filmCategory.setCategory(category);
            filmCategory.setLastUpdate(LocalDateTime.now());
            filmCategoryRepository.save(filmCategory);
            return ResponseEntity.ok(filmMapper.toDto(film));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film category: " + e.getMessage());
        }
    }
}