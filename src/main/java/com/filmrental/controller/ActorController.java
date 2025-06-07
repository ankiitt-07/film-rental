package com.filmrental.controller;

import com.filmrental.mapper.ActorMapper;
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

    @Autowired
    private ActorMapper mapper;

    @PostMapping("/post")
    public ResponseEntity<String> addnewactor(@RequestBody ActorDTO actorDTO) {
        // Check if actor already exists by firstName and lastName (case-insensitive)
        boolean exists = actorRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(
                actorDTO.getFirstName(), actorDTO.getLastName());

        if (exists) {
            return ResponseEntity.status(409).body("Actor already exists");
        }

        Actor actor = mapper.toActorEntity(actorDTO);
        actorRepository.save(actor);
        return ResponseEntity.ok("New actor added");
    }



    @GetMapping("/lastname/{ln}")
    public ResponseEntity<List<ActorDTO>> searchByLastName(@PathVariable String ln) {
        List<ActorDTO> listByLastName = actorRepository.findByLastNameIgnoreCase(ln)
                .stream().map(mapper::toActorDto).toList();

        return ResponseEntity.ok(listByLastName);
    }

    @GetMapping("/firstname/{fn}")
    public ResponseEntity<List<ActorDTO>> searchByFirstName(@PathVariable String fn) {
        List<ActorDTO> listByFirstName = actorRepository.findByFirstNameIgnoreCase(fn)
                .stream().map(mapper::toActorDto).toList();

        return ResponseEntity.ok(listByFirstName);
    }
}
