package com.filmrental.mapper;

import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.entity.Actor;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ActorMapper {

    public ActorDTO toDto(Actor actor) {
        if (actor == null) {
            return null;
        }
        ActorDTO dto = new ActorDTO();
        dto.setActorId(actor.getActorId());
        dto.setFirstName(actor.getFirstName());
        dto.setLastName(actor.getLastName());
        dto.setFilmIds(actor.getFilms().stream()
                .map(film -> film.getFilmId())
                .collect(Collectors.toList()));
        return dto;
    }

    public Actor toEntity(ActorDTO dto) {
        if (dto == null) {
            return null;
        }
        Actor actor = new Actor();
        actor.setActorId(dto.getActorId());
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        return actor;
    }
}