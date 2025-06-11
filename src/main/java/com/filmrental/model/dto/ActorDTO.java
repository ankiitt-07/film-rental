package com.filmrental.model.dto;

import lombok.Data;
import java.util.List;

@Data
public class ActorDTO {
    private Integer actorId;
    private String firstName;
    private String lastName;
    private List<Integer> filmIds;
}