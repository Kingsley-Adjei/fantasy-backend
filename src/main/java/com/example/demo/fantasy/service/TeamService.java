package com.example.demo.fantasy.service;

import com.example.demo.fantasy.dto.TransferRequest;
import com.example.demo.fantasy.dto.LineupRequest; // Ensure this DTO exists!
import com.example.demo.fantasy.entity.*;
import com.example.demo.fantasy.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PlayerRepository playerRepository;
    private final GameweekRepository gameweekRepository; // Added this!
    private final SquadPlayerRepository squadPlayerRepository; // Added this!

    // --- 1. SQUAD CREATION ---
    @Transactional
    public Team createInitialSquad(User user, String teamName, List<Long> playerIds) {
        if (teamRepository.existsByTeamName(teamName)) {
            throw new RuntimeException("Team name already exists!");
        }

        List<Player> selectedPlayers = playerRepository.findAllById(playerIds);

        if (selectedPlayers.size() != 15) {
            throw new RuntimeException("A squad must have exactly 15 players.");
        }

        double totalCost = selectedPlayers.stream().mapToDouble(Player::getPrice).sum();
        if (totalCost > 100.0) {
            throw new RuntimeException("Squad exceeds budget of 100 Cedis!");
        }

        Team newTeam = Team.builder()
                .user(user)
                .teamName(teamName)
                .remainingBudget(100.0 - totalCost)
                .totalPoints(0)
                .build();

        // Save team first so it has an ID
        Team savedTeam = teamRepository.save(newTeam);

        // Convert Players to SquadPlayers (Bridge Table)
        for (int i = 0; i < selectedPlayers.size(); i++) {
            boolean starter = i < 11; // First 11 are starters by default
            SquadPlayer sp = SquadPlayer.builder()
                    .team(savedTeam)
                    .player(selectedPlayers.get(i))
                    .isOnPitch(starter)
                    .benchOrder(starter ? 0 : (i - 10))
                    .build();
            squadPlayerRepository.save(sp);
        }

        return savedTeam;
    }

    // --- 2. THE MISSING UPDATE LINEUP METHOD ---
    @Transactional
    public void updateLineup(User user, LineupRequest request) {
        // DEADLINE GUARD
        Gameweek currentGw = gameweekRepository.findByIsCurrentTrue();
        if (currentGw != null && LocalDateTime.now().isAfter(currentGw.getDeadline())) {
            throw new RuntimeException("GAMEWEEK_LOCKED: Deadline has passed!");
        }

        Team team = teamRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("TEAM_NOT_FOUND"));

        List<SquadPlayer> allSquadPlayers = team.getSquadPlayers();

        for (SquadPlayer sp : allSquadPlayers) {
            Long pid = sp.getPlayer().getId();
            if (request.getPitchIds().contains(pid)) {
                sp.setOnPitch(true);
                sp.setBenchOrder(0);
            } else if (request.getBenchIds().contains(pid)) {
                sp.setOnPitch(false);
                sp.setBenchOrder(request.getBenchIds().indexOf(pid) + 1);
            }
        }
        teamRepository.save(team);
    }

    // --- 3. THE TRANSFER LOGIC ---
    @Transactional
    public void performTransfer(User user, TransferRequest request) {
        // DEADLINE GUARD
        Gameweek currentGw = gameweekRepository.findByIsCurrentTrue();
        if (currentGw != null && LocalDateTime.now().isAfter(currentGw.getDeadline())) {
            throw new RuntimeException("GAMEWEEK_LOCKED: Deadline has passed!");
        }

        Team team = teamRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("TEAM_NOT_FOUND"));

        Player playerOut = playerRepository.findById(request.getPlayerOutId()).orElseThrow();
        Player playerIn = playerRepository.findById(request.getPlayerInId()).orElseThrow();

        SquadPlayer assignment = team.getSquadPlayers().stream()
                .filter(sp -> sp.getPlayer().getId().equals(playerOut.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("PLAYER_NOT_IN_SQUAD"));

        if (!playerOut.getPosition().equals(playerIn.getPosition())) {
            throw new RuntimeException("Position mismatch!");
        }

        double availableFunds = team.getRemainingBudget() + playerOut.getPrice();
        if (availableFunds < playerIn.getPrice()) {
            throw new RuntimeException("Insufficient budget!");
        }

        assignment.setPlayer(playerIn);
        team.setRemainingBudget(availableFunds - playerIn.getPrice());

        teamRepository.save(team);
    }
    // --- 4. GET TEAM BY USER ---
    public java.util.Optional<Team> getTeamByUser(User user) {
        // This connects the User from the Security Token to their Team in the DB
        return teamRepository.findByUserId(user.getId());
    }
}