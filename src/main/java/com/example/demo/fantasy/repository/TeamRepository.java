package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {
    Optional<Team> findByUserId(UUID userId);
    // Find all teams where the 'players' list contains this ID
    List<Team> findAllByPlayersId(Long playerId);
    boolean existsByTeamName(String teamName);
}