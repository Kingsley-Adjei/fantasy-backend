package com.example.demo.fantasy.service;

import com.example.demo.fantasy.entity.Player;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.repository.PlayerRepository;
import com.example.demo.fantasy.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointsService {

    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void updatePlayerPoints(Long playerId, Integer newPoints) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("PLAYER_NOT_FOUND"));

        // 1. Add to player's season total
        player.setTotalPoints(player.getTotalPoints() + newPoints);
        playerRepository.save(player);

        // 2. Find all teams that own this player
        List<Team> teamsWithPlayer = teamRepository.findAllByPlayersId(playerId);

        // 3. Update each team's total
        for (Team team : teamsWithPlayer) {
            // Note: In a real app, you only add points if the player was in the STARTING 11
            team.setTotalPoints(team.getTotalPoints() + newPoints);
            teamRepository.save(team);
        }
    }
}