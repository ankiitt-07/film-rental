package com.filmrental.mapper;

import com.filmrental.model.dto.ActorDTO;
import com.filmrental.model.entity.Actor;

public class ActorMapper {

    public static ActorDTO toDto(Actor actor) {
        if (actor == null) return null;

        return new ActorDTO(
                actor.getFirstName(),
                actor.getLastName(),
                actor.getLastUpdate()
        );
    }

    public static Actor toEntity(ActorDTO dto) {
        if (dto == null) return null;

        Actor actor = new Actor();
        actor.setFirstName(dto.getFirstName());
        actor.setLastName(dto.getLastName());
        actor.setLastUpdate(dto.getLastUpdate());
        return actor;
    }
}