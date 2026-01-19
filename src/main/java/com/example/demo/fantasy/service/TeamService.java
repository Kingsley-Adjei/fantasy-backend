package com.example.demo.fantasy.service;

import com.example.demo.fantasy.entity.Player;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.entity.User;
import com.example.demo.fantasy.repository.PlayerRepository;
import com.example.demo.fantasy.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;

    @Transactional
    public Team createInitialSquad(User user, String teamName, List<UUID> playerIds) {
        // 1. Check if team name is taken
        if (teamRepository.existsByTeamName(teamName)) {
            throw new RuntimeException("Team name already exists!");
        }

        // 2. Fetch players from DB
        List<Player> selectedPlayers = playerRepository.findAllById(playerIds);

        // 3. VALIDATION: Check squad size
        if (selectedPlayers.size() != 15) {
            throw new RuntimeException("A squad must have exactly 15 players.");
        }

        // 4. VALIDATION: Check budget
        double totalCost = selectedPlayers.stream().mapToDouble(Player::getPrice).sum();
        if (totalCost > 100.0) {
            throw new RuntimeException("Squad exceeds budget of 100 Cedis!");
        }

        // 5. Save the Team
        Team newTeam = Team.builder()
                .user(user)
                .teamName(teamName)
                .players(selectedPlayers)
                .remainingBudget(100.0 - totalCost)
                .build();

        return teamRepository.save(newTeam);
    }
}