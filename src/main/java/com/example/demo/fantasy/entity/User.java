package com.example.demo.fantasy.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter // From Lombok
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_verified")
    private boolean is_verified = false;

    // This links the User to the Team table
    // mappedBy refers to the 'user' field in the Team entity
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Team team;

    private LocalDateTime createdAt = LocalDateTime.now();
}
