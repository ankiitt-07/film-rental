package com.filmrental.mapper;

import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.entity.Actor;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    public ActorDTO toActorDto(Actor actor) {
        if (actor == null) return null;
        ActorDTO actorDTO = new ActorDTO();
        actorDTO.setActorId(actor.getActorId());
        actorDTO.setFirstName(actor.getFirstName());
        actorDTO.setLastName(actor.getLastName());
        actorDTO.setLastUpdate(actor.getLastUpdate());
        return actorDTO;
    }

    public Actor toActorEntity(ActorDTO dto) {
        if (dto == null) return null;
        Actor actor = new Actor();
        actor.setActorId(dto.getActorId());
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        actor.setLastUpdate(dto.getLastUpdate());
        return actor;
    }
}