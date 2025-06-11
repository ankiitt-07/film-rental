package com.filmrental.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonBackReference;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "actor")
@Getter
@Setter
public class Actor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id")
    private Integer actorId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "film_actor",
            joinColumns = @JoinColumn(name = "actor_id"),
            inverseJoinColumns = @JoinColumn(name = "film_id")
    )
    @JsonBackReference
    private Set<Film> films = new HashSet<>();

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdate = LocalDateTime.now();
    }
}