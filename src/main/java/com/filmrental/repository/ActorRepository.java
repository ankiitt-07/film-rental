package com.filmrental.repository;

import com.filmrental.model.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByFirstName(String firstName);

    List<Actor> findByLastName(String lastName);

    boolean findByFirstNameAndLastName(String firstName, String lastName);

}