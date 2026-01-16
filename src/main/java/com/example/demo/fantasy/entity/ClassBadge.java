package com.example.demo.fantasy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "class_badges")
@Getter
@Setter
public class ClassBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String badgeName; // e.g., "CS1"
    private String badgeIconUrl; // Link to the badge image
}
