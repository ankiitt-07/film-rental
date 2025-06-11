package com.filmrental.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "film_category")
@Data
public class FilmCategory {
    @EmbeddedId
    private FilmCategoryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("filmId")
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdate = LocalDateTime.now();
    }
}