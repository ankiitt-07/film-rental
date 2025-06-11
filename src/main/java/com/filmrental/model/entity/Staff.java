package com.filmrental.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "staff")
@Data
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Integer staffId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String email;

    private Boolean active;

    private String picture;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate = LocalDateTime.now();

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "staff")
    private List<Rental> rentals;

    @OneToMany(mappedBy = "staff")
    private List<Payment> payments;
}