package com.filmrental.mapper;

import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.entity.Actor;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {
    public Actor toActorEntity(ActorDTO actorDTO){
        Actor actor = new Actor();
        actor.setFirstName(actorDTO.getFirstName());
        actor.setLastName(actorDTO.getLastName());
        return actor;
    }

    public ActorDTO toActorDto(Actor actor){
        return new ActorDTO(actor.getFirstName(), actor.getLastName(),actor.getLastUpdate());
    }
}
