package com.example.demo.fantasy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "teams")
@Getter
@Setter
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String teamName;

    private Integer totalPoints = 0;

    // This is the Many-to-One magic
    @ManyToOne
    @JoinColumn(name = "badge_id")
    private ClassBadge badge;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
