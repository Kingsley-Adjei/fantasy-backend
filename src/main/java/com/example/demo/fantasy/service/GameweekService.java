package com.example.demo.fantasy.service;

import com.example.demo.fantasy.entity.Gameweek;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.repository.GameweekRepository;
import com.example.demo.fantasy.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameweekService {

    private final TeamRepository teamRepository;
    private final GameweekRepository gameweekRepository;

    @Transactional
    public void finalizeGameweek() {
        // 1. Get the current active week
        Gameweek currentGw = gameweekRepository.findByIsCurrentTrue();

        // 2. Get all teams and calculate their scores
        List<Team> allTeams = teamRepository.findAll();
        for (Team team : allTeams) {
            // Only sum points for players where isOnPitch = true
            int pointsEarned = team.getSquadPlayers().stream()
                    .filter(sp -> sp.isOnPitch())
                    .mapToInt(sp -> sp.getPlayer().getPointsThisGw())
                    .sum();

            team.setTotalPoints(team.getTotalPoints() + pointsEarned);
        }

        // 3. Mark week as finished and move to next (For demo purposes)
        currentGw.setCurrent(false);
        gameweekRepository.save(currentGw);

        // Logic to set next week as Current could go here
    }
}