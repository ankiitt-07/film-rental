package com.filmrental.repository;

import com.filmrental.model.entity.Language;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LanguageRepository extends JpaRepository<Language, Integer> {
    Optional<Language> findByName(String name);
}