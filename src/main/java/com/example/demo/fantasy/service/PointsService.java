package com.example.demo.fantasy.service;

import com.example.demo.fantasy.entity.Player;
import com.example.demo.fantasy.entity.SquadPlayer;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.repository.PlayerRepository;
import com.example.demo.fantasy.repository.SquadPlayerRepository;
import com.example.demo.fantasy.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointsService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final SquadPlayerRepository squadPlayerRepository;

    @Transactional
    public void updatePlayerPoints(Long playerId, Integer newPoints) {
        // 1. Update the player's total points in the master list
        Player player = playerRepository.findById(playerId).orElseThrow();
        player.setTotalPoints(player.getTotalPoints() + newPoints);

        // 2. Find all 'SquadPlayer' entries for this player where they are STARTING
        List<SquadPlayer> starters = squadPlayerRepository.findByPlayerIdAndIsOnPitchTrue(playerId);

        // Update the player's performance for THIS week
        player.setPointsThisGw(player.getPointsThisGw() + newPoints);

        // Also add to their career total
        player.setTotalPoints(player.getTotalPoints() + newPoints);

        // 3. Only add points to teams where they were in the Starting XI
        for (SquadPlayer starter : starters) {
            Team team = starter.getTeam();
            team.setTotalPoints(team.getTotalPoints() + newPoints);
            teamRepository.save(team);
        }
    }
}