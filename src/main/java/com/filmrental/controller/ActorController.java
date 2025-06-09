package com.filmrental.controller;

import com.filmrental.mapper.ActorMapper;
import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.dto.FilmDTO;
import com.filmrental.model.entity.Actor;
import com.filmrental.model.entity.Film;
import com.filmrental.repository.ActorRepository;
import com.filmrental.repository.FilmRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;
    @Autowired
    private final ActorMapper mapper;
    @Autowired
    private FilmRepository filmRepository;

    public ActorController(ActorMapper mapper) {
        this.mapper = mapper;
    }

    @PostMapping("/post")
    public ResponseEntity<String> addnewactor(@RequestBody ActorDTO actorDTO) {
        try {
            boolean exists = actorRepository.findByFirstNameAndLastName(
                    actorDTO.getFirstName(), actorDTO.getLastName());

            if (exists) {
                return ResponseEntity.status(409).body("Actor already exists");
            }

            Actor actor = mapper.toActorEntity(actorDTO);
            actorRepository.save(actor);
            return ResponseEntity.ok("New actor added");
        } catch (Exception e) {
            throw new RuntimeException("Failed to add actor", e);
        }
    }

    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<ActorDTO>> searchByLastName(@PathVariable String ln) {
        try {
            List<ActorDTO> listByLastName = actorRepository.findByLastName(ln)
                    .stream()
                    .map(mapper::toActorDto)
                    .toList();

            return ResponseEntity.ok(listByLastName);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching actors by last name", e);
        }
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<ActorDTO>> searchByFirstName(@PathVariable String fn) {
        try {
            List<ActorDTO> listByFirstName = actorRepository.findByFirstName(fn)
                    .stream().map(mapper::toActorDto).toList();

            return ResponseEntity.ok(listByFirstName);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching actors by first name", e);
        }
    }


    @PutMapping("/update/lastname/{id}")
    public ResponseEntity<ActorDTO> updateByLastName(@PathVariable Long id, @RequestBody ActorDTO actorDTO) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));
        actor.setLastName(actorDTO.getLastName());
        actor.setLastUpdate(LocalDateTime.now());
        actorRepository.save(actor);
        return ResponseEntity.ok(mapper.toActorDto(actor));
    }


    @PutMapping("/update/lastname/{id}")
    public ResponseEntity<ActorDTO> updateByFirstName(@PathVariable Long id, @RequestBody ActorDTO actorDTO) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));
        actor.setFirstName(actorDTO.getFirstName());
        actor.setLastUpdate(LocalDateTime.now());
        actorRepository.save(actor);
        return ResponseEntity.ok(mapper.toActorDto(actor));
    }



    @PutMapping("/{id}/film")
    public ResponseEntity<FilmDTO> assignFilmToActor(@PathVariable Long id, @RequestBody FilmDTO filmDTO) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));
        Film film = filmRepository.findById(filmDTO.filmId())
                .orElseThrow(() -> new EntityNotFoundException("FIlm not found by Id " + id));

        actor.getFilms().add(film);
        actor.setLastUpdate(LocalDateTime.now());
        actorRepository.save(actor);

        List<FilmDTO> filmdto = actor.getFilms()
                .stream().map(mapper::toFilmDTO)
                .toList();
        return ResponseEntity.ok(filmDTO);
    }


    @GetMapping("/{id}/films")
    public ResponseEntity<List<FilmDTO>> filmsByActorId(@PathVariable Long id) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));

        List<FilmDTO> filmdto = actor.getFilms()
                .stream().map(mapper::toFilmDTO)
                .toList();

        return ResponseEntity.ok(filmdto);
    }

    @GetMapping("/toptenbyfilmcount")
    public ResponseEntity<List<Object[]>> topTenActorsByFilmCount() {
        List<Actor> allActors = actorRepository.findAllWithFilms();

        List<Object[]> topActors = allActors.stream()
                .map(actor -> new Object[]{
                        actor.getFirstName(),
                        actor.getLastName(),
                        actor.getFilms().size()
                })
                .sorted((a1, a2) -> Integer.compare((int) a2[2], (int) a1[2])) // descending by film count
                .limit(10)
                .toList();

        return ResponseEntity.ok(topActors);
    }


}
