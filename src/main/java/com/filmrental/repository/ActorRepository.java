package com.filmrental.repository;

import com.filmrental.model.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByFirstName(String firstName);

    List<Actor> findByLastName(String lastName);

    boolean findByFirstNameAndLastName(String firstName, String lastName);

    List<Actor> findAllWithFilms();
}