package com.example.demo.fantasy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor // Required for JPA
@AllArgsConstructor // Required for Builder
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String teamName;

    private Integer totalPoints = 0;

    private Double remainingBudget = 100.0;

    @ManyToMany
    @JoinTable(
            name = "team_players",
            joinColumns = @JoinColumn(name = "team_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id")
    )
    private java.util.List<Player> players; // Your squad of 15

    @ManyToOne
    @JoinColumn(name = "badge_id")
    private ClassBadge badge;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SquadPlayer> squadPlayers;

    // Helper method to get only starters for point calculation
    public List<Player> getStartingEleven() {
        return squadPlayers.stream()
                .filter(SquadPlayer::isOnPitch)
                .map(SquadPlayer::getPlayer)
                .toList();
    }
}
