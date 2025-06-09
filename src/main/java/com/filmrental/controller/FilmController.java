package com.filmrental.controller;

import com.filmrental.exception.ResourceNotFoundException;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Film;
import com.filmrental.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    @PostMapping("/post")
    public ResponseEntity<FilmDTO> addFilm(@RequestBody FilmDTO filmDTO) {
        try {
            if (filmDTO == null) {
                throw new IllegalArgumentException("Film data cannot be null");
            }
            Film film = filmMapper.toEntity(filmDTO);
            Film savedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(savedFilm));
        } catch (IllegalArgumentException e) {
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
    public ResponseEntity<?> getActorsByFilm(@PathVariable Integer id) {
        try {
            throw new UnsupportedOperationException("Actor retrieval not implemented");
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve actors for film: " + e.getMessage());
        }
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getFilmsByCategory(@PathVariable String category) {
        try {
            throw new UnsupportedOperationException("Category retrieval not implemented");
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films by category: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/actor")
    public ResponseEntity<?> assignActorToFilm(@PathVariable Integer id, @RequestBody Object actor) {
        try {
            throw new UnsupportedOperationException("Actor assignment not implemented");
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
            Film updatedFilm = filmRepository.save(film);
            return ResponseEntity.ok(filmMapper.toDto(updatedFilm));
        } catch (IllegalArgumentException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film release year: " + e.getMessage());
        }
    }

    @PutMapping("/update/rentaldurtion/{id}")
    public ResponseEntity<FilmDTO> updateFilmRentalDuration(@PathVariable Integer id, @RequestBody Integer rentalDuration) {
        try {
            if (rentalDuration == null || rentalDuration < 0) {
                throw new IllegalArgumentException("Rental duration must be non-negative");
            }
            Film film = filmRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Film not found with ID: " + id));
            film.setRentalDuration(rentalDuration);
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
    public ResponseEntity<?> updateFilmCategory(@PathVariable Integer id, @RequestBody String category) {
        try {
            throw new UnsupportedOperationException("Category update not implemented");
        } catch (Exception e) {
            throw new RuntimeException("Failed to update film category: " + e.getMessage());
        }
    }
}