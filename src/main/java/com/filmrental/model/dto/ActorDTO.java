package com.filmrental.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO {
    private Integer actorId;
    private String firstName;
    private String lastName;
    private LocalDateTime lastUpdate;
}