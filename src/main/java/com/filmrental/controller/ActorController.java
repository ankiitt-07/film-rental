package com.filmrental.controller;

import com.filmrental.mapper.ActorMapper;
import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.entity.Actor;
import com.filmrental.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    @Autowired
    private ActorRepository actorRepository;

    private final ActorMapper mapper;

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
}
