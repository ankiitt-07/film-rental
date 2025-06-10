package com.filmrental.repository;

import com.filmrental.model.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    List<Actor> findByFirstName(String firstName);
    List<Actor> findByLastName(String lastName);
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    Optional<Actor> findByFirstNameAndLastName(String firstName, String lastName);
}