package com.example.demo.fantasy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "leagues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String joinCode; // e.g., "ABC123"

    @ManyToMany
    @JoinTable(
            name = "league_members",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    private List<Team> members; // Only teams in this league
}
