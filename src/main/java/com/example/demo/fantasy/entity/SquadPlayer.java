package com.example.demo.fantasy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SquadPlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;

    private boolean isOnPitch; // true = Starter, false = Bench

    private Integer benchOrder; // 0 for starters, 1-3 for bench priority
}