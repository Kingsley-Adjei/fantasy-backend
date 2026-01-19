package com.example.demo.fantasy.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY.IDENTITY)
    private Long id;

    private String name;
    private String position; // GK, DEF, MID, FWD
    private String realClub; // e.g., "Man City"
    private Double price;    // e.g., 12.5 (Cedis)
    private Integer totalPoints = 0;
}