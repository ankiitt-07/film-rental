package com.filmrental.repository;

import com.filmrental.model.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByFirstName(String firstName);
    List<Actor> findByLastName(String lastName);
    List<Actor> findByFirstNameAndLastName(String firstName, String lastName);
}