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

    @ManyToOne
    @MapsId("filmId")
    @JoinColumn(name = "film_id")
    private Film film;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;
}

@Embeddable
@Data
public class FilmCategoryId implements java.io.Serializable {
    @Column(name = "film_id")
    private Integer filmId;

    @Column(name = "category_id")
    private Integer categoryId;
}