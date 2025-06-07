package com.filmrental.controller;

import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Film;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/films")
public class FilmController {

    @Autowired
    private FilmRepository filmRepository;

    @PostMapping("/post")
    public ResponseEntity<FilmDTO> addFilm(@RequestBody FilmDTO filmDTO) {
        Film film = FilmMapper.toEntity(filmDTO);
        Film savedFilm = filmRepository.save(film);
        return ResponseEntity.ok(FilmMapper.toDto(savedFilm));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<FilmDTO>> findByTitle(@PathVariable String title) {
        List<Film> films = filmRepository.findByTitleContainingIgnoreCase(title);
        List<FilmDTO> filmDTOs = films.stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(filmDTOs);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<FilmDTO>> findByReleaseYear(@PathVariable int year) {
        List<Film> films = filmRepository.findByReleaseYear(Year.of(year));
        List<FilmDTO> filmDTOs = films.stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(filmDTOs);
    }

    @GetMapping("/duration/gt/{rd}")
    public ResponseEntity<List<FilmDTO>> findByRentalDurationGreaterThan(@PathVariable int rd) {
        List<Film> films = filmRepository.findByRentalDurationGreaterThan(rd);
        List<FilmDTO> filmDTOs = films.stream()
                .map(FilmMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(filmDTOs);
    }
}