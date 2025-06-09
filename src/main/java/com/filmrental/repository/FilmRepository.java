package com.filmrental.repository;

import com.filmrental.model.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface FilmRepository extends JpaRepository<Film, Integer> {
    Optional<Film> findByTitle(String title);
    List<Film> findByLanguageLanguageId(Integer languageId);
    List<Film> findByReleaseYear(int releaseYear);
    List<Film> findByRentalDurationGreaterThan(Integer rentalDuration);
    List<Film> findByRentalDurationLessThan(Integer rentalDuration);
    List<Film> findByRentalRateGreaterThan(BigDecimal rate);
    List<Film> findByRentalRateLessThan(BigDecimal rate);
    List<Film> findByLengthGreaterThan(Integer length);
    List<Film> findByLengthLessThan(Integer length);
    List<Film> findByRatingGreaterThan(String rating);
    List<Film> findByRatingLessThan(String rating);
    List<Film> findByReleaseYearGreaterThanEqualAndReleaseYearLessThanEqual(int from, int to);
    List<Film> findByActorsActorId(Integer actorId);
    List<Film> findByCategoryName(String categoryName);
}