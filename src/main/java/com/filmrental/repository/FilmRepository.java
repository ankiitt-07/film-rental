package com.filmrental.repository;

import com.filmrental.model.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Integer> {
    Optional<Film> findByTitle(String title);
    List<Film> findByTitleContainingIgnoreCase(String title);
    List<Film> findByReleaseYear(Year releaseYear);
    List<Film> findByRentalDurationGreaterThan(Integer rentalDuration);
    List<Film> findByRentalRateGreaterThan(BigDecimal rentalRate);
    List<Film> findByLengthGreaterThan(Integer length);
    List<Film> findByRentalDurationLessThan(Integer rentalDuration);
    List<Film> findByRentalRateLessThan(BigDecimal rentalRate);
    List<Film> findByLengthLessThan(Integer length);
    List<Film> findByReleaseYearBetween(Year startYear, Year endYear);
    List<Film> findByRatingLessThan(String rating);
    List<Film> findByRatingGreaterThan(String rating);
    List<Film> findByLanguage_Name(String name);
    List<Film> findByFilmCategories_Category_Name(String categoryName);

    @Query("SELECT f.releaseYear, COUNT(f) FROM Film f GROUP BY f.releaseYear ORDER BY f.releaseYear")
    List<Object[]> findFilmCountByYear();
}