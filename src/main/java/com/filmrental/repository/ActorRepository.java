package com.filmrental.repository;

import com.filmrental.model.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    List<Actor> findByLastNameIgnoreCase(String lastName);
    List<Actor> findByFirstNameIgnoreCase(String firstName);
    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(String firstName, String lastName);

}
