package com.filmrental.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "create_date", nullable = false)
    private LocalDate createDate;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Rental> rentals;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Payment> payments;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.lastUpdate = LocalDateTime.now();
    }
}