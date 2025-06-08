package com.filmrental.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer category_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime last_update;

    @OneToMany(mappedBy = "category")
    private List<FilmCategory> filmCategories;
}