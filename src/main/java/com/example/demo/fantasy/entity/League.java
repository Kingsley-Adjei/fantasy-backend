package com.example.demo.fantasy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    private String joinCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    @JsonIgnore
    private User createdBy;  // tracks admin

    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinTable(
            name = "league_members",
            joinColumns = @JoinColumn(name = "league_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id")
    )
    @Builder.Default
    private List<Team> members = new ArrayList<>();

    @PrePersist
    public void generateJoinCode() {
        if (this.joinCode == null) {
            this.joinCode = java.util.UUID.randomUUID()
                    .toString()
                    .substring(0, 6)
                    .toUpperCase();
        }
    }
    @Column(name = "is_global", nullable = false)
    @Builder.Default
    private boolean isGlobal = false;
}