package com.example.demo.fantasy.repository;

import com.example.demo.fantasy.entity.SquadPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SquadPlayerRepository extends JpaRepository<SquadPlayer, Long> {

    // This finds only the players who are actually STARTING (isOnPitch = true)
    List<SquadPlayer> findByPlayerIdAndIsOnPitchTrue(Long playerId);
}