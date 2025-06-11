package com.filmrental.controller;

import com.filmrental.mapper.ActorMapper;
import com.filmrental.mapper.FilmMapper;
import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Actor;
import com.filmrental.model.entity.Film;
import com.filmrental.repository.ActorRepository;
import com.filmrental.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    @GetMapping("/all")
    public ResponseEntity<Page<ActorDTO>> getAllActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Actor> actors = actorRepository.findAll(pageable);
            return actors.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(actors.map(actorMapper::toDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/post")
    public ResponseEntity<String> addNewActor(@RequestBody ActorDTO actorDTO) {
        try {
            if (actorDTO.getFirstName() == null || actorDTO.getLastName() == null) {
                throw new IllegalArgumentException("First name and last name are required");
            }
            if (actorRepository.existsByFirstNameAndLastName(actorDTO.getFirstName(), actorDTO.getLastName())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Actor already exists");
            }
            Actor actor = actorMapper.toEntity(actorDTO);
            actor.setLastUpdate(LocalDateTime.now());
            actorRepository.save(actor);
            return ResponseEntity.status(HttpStatus.CREATED).body("Record Created Successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating actor");
        }
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<ActorDTO>> searchByLastName(@PathVariable String ln) {
        try {
            if (ln == null || ln.trim().isEmpty()) {
                throw new IllegalArgumentException("Last name cannot be empty");
            }
            List<Actor> actors = actorRepository.findByLastNameContainingIgnoreCase(ln);
            return actors.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(actors.stream().map(actorMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<ActorDTO>> searchByFirstName(@PathVariable String fn) {
        try {
            if (fn == null || fn.trim().isEmpty()) {
                throw new IllegalArgumentException("First name cannot be empty");
            }
            List<Actor> actors = actorRepository.findByFirstNameContainingIgnoreCase(fn);
            return actors.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(actors.stream().map(actorMapper::toDto).collect(Collectors.toList()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/lastname/{id}")
    public ResponseEntity<ActorDTO> updateLastName(@PathVariable Integer id, @RequestBody String lastName) {
        try {
            if (id <= 0 || lastName == null || lastName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid ID or last name");
            }
            Actor actor = actorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Actor not found with ID: " + id));
            actor.setLastName(lastName);
            actor.setLastUpdate(LocalDateTime.now());
            Actor updatedActor = actorRepository.save(actor);
            return ResponseEntity.ok(actorMapper.toDto(updatedActor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/update/firstname/{id}")
    public ResponseEntity<ActorDTO> updateFirstName(@PathVariable Integer id, @RequestBody String firstName) {
        try {
            if (id <= 0 || firstName == null || firstName.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid ID or first name");
            }
            Actor actor = actorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Actor not found with ID: " + id));
            actor.setFirstName(firstName);
            actor.setLastUpdate(LocalDateTime.now());
            Actor updatedActor = actorRepository.save(actor);
            return ResponseEntity.ok(actorMapper.toDto(updatedActor));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}/films")
    public ResponseEntity<List<FilmDTO>> filmsByActorId(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid actor ID");
            }
            Actor actor = actorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Actor not found with ID: " + id));
            List<FilmDTO> filmDTOs = actor.getFilms().stream().map(filmMapper::toDto).collect(Collectors.toList());
            return filmDTOs.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(filmDTOs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}/film")
    public ResponseEntity<List<FilmDTO>> assignFilmToActor(@PathVariable Integer id, @RequestBody Integer filmId) {
        try {
            if (id <= 0 || filmId <= 0) {
                throw new IllegalArgumentException("Invalid actor or film ID");
            }
            Actor actor = actorRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Actor not found with ID: " + id));
            Film film = filmRepository.findById(filmId).orElseThrow(() -> new IllegalArgumentException("Film not found with ID: " + filmId));
            actor.getFilms().add(film);
            actor.setLastUpdate(LocalDateTime.now());
            actorRepository.save(actor);
            List<FilmDTO> updatedFilms = actor.getFilms().stream().map(filmMapper::toDto).collect(Collectors.toList());
            return ResponseEntity.ok(updatedFilms);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/toptenbyfilmcount")
    public ResponseEntity<List<Object[]>> topTenActorsByFilmCount() {
        try {
            Pageable pageable = PageRequest.of(0, 10);
            Page<Object[]> results = actorRepository.findTopActorsByFilmCount(pageable);
            // Transform [Actor, filmCount] to [actorId, fullName, filmCount]
            List<Object[]> transformedResults = results.getContent().stream()
                    .map(row -> {
                        Actor actor = (Actor) row[0];
                        Long filmCount = (Long) row[1];
                        return new Object[]{actor.getActorId(), actor.getFirstName() + " " + actor.getLastName(), filmCount};
                    })
                    .collect(Collectors.toList());
            return transformedResults.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(null) : ResponseEntity.ok(transformedResults);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}