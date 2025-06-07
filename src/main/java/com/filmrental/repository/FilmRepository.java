package com.filmrental.repository;

import com.filmrental.model.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Year;
import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    List<Film> findByTitleContainingIgnoreCase(String title);

    List<Film> findByReleaseYear(Year year);

    List<Film> findByRentalDurationGreaterThan(int rentalDuration);
}

