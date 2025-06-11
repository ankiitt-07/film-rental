package com.filmrental.controller;

import com.filmrental.mapper.ActorMapper;
import com.filmrental.mapper.FilmMapper;
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
    private ActorMapper actorMapper;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    public ActorController(ActorMapper actorMapper, FilmMapper filmMapper) {
        this.actorMapper = actorMapper;
        this.filmMapper = filmMapper;
    }

    @PostMapping("/post")
    public ResponseEntity<String> addNewActor(@RequestBody ActorDTO actorDTO) {
        try {
            if (actorRepository.existsByFirstNameAndLastName(actorDTO.getFirstName(), actorDTO.getLastName())) {
                return ResponseEntity.status(409).body("Actor already exists");
            }

            Actor actor = actorMapper.toActorEntity(actorDTO);
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
                    .map(actorMapper::toActorDto)
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
                    .stream()
                    .map(actorMapper::toActorDto)
                    .toList();

            return ResponseEntity.ok(listByFirstName);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching actors by first name", e);
        }
    }

    @PutMapping("/update/lastname/{id}")
    public ResponseEntity<ActorDTO> updateByLastName(@PathVariable Integer id, @RequestBody ActorDTO actorDTO) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));
        actor.setLastName(actorDTO.getLastName());
        actor.setLastUpdate(LocalDateTime.now());
        actorRepository.save(actor);
        return ResponseEntity.ok(actorMapper.toActorDto(actor));
    }

    @PutMapping("/update/firstname/{id}")
    public ResponseEntity<ActorDTO> updateByFirstName(@PathVariable Integer id, @RequestBody ActorDTO actorDTO) {
        Actor actor = actorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));
        actor.setFirstName(actorDTO.getFirstName());
        actor.setLastUpdate(LocalDateTime.now());
        actorRepository.save(actor);
        return ResponseEntity.ok(actorMapper.toActorDto(actor));
    }

    @PutMapping("/{id}/film")
    public ResponseEntity<FilmDTO> assignFilmToActor(@PathVariable Integer id, @RequestBody FilmDTO filmDTO) {
        try {
            Actor actor = actorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));
            Film film = filmRepository.findById(filmDTO.getFilmId())
                    .orElseThrow(() -> new EntityNotFoundException("Film not found with Id " + filmDTO.getFilmId()));

            actor.getFilms().add(film);
            actor.setLastUpdate(LocalDateTime.now());
            actorRepository.save(actor);

            return ResponseEntity.ok(filmDTO);
        } catch (Exception e) {
            throw new RuntimeException("Failed to assign film to actor", e);
        }
    }

    @GetMapping("/{id}/films")
    public ResponseEntity<List<FilmDTO>> filmsByActorId(@PathVariable Integer id) {
        try {
            Actor actor = actorRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Actor not found with Id " + id));

            List<FilmDTO> filmDTOs = actor.getFilms()
                    .stream()
                    .map(filmMapper::toDto)
                    .toList();

            return ResponseEntity.ok(filmDTOs);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve films for actor", e);
        }
    }

    @GetMapping("/toptenbyfilmcount")
    public ResponseEntity<List<Object[]>> topTenActorsByFilmCount() {
        try {
            List<Actor> allActors = actorRepository.findAll();

            List<Object[]> topActors = allActors.stream()
                    .map(actor -> new Object[]{
                            actor.getFirstName(),
                            actor.getLastName(),
                            actor.getFilms().size()
                    })
                    .sorted((a1, a2) -> Integer.compare((Integer) a2[2], (Integer) a1[2])) // descending by film count
                    .limit(10)
                    .toList();

            return ResponseEntity.ok(topActors);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve top ten actors by film count", e);
        }
    }
}