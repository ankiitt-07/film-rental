package com.filmrental.repository;

import com.filmrental.model.entity.Actor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Integer> {
    List<Actor> findByFirstNameContainingIgnoreCase(String firstName);
    List<Actor> findByLastNameContainingIgnoreCase(String lastName);
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    Optional<Actor> findByFirstNameAndLastName(String firstName, String lastName);

    // Alternative query: Returns [Actor, filmCount] for top actors by film count
    @Query("SELECT a, COUNT(f) " +
            "FROM Actor a LEFT JOIN a.films f " +
            "GROUP BY a.actorId " +
            "ORDER BY COUNT(f) DESC")
    Page<Object[]> findTopActorsByFilmCount(Pageable pageable);
}