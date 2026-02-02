package com.example.demo.fantasy.service;

import com.example.demo.fantasy.dto.LeagueResponseDTO;
import com.example.demo.fantasy.dto.TeamStandingDTO;
import com.example.demo.fantasy.entity.League;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.entity.User;
import com.example.demo.fantasy.repository.LeagueRepository;
import com.example.demo.fantasy.repository.TeamRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LeagueService {
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public League createLeague(User user, String leagueName) {
        Team creatorTeam = teamRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("CREATE_TEAM_FIRST"));

        String joinCode = UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        League league = League.builder()
                .name(leagueName)
                .joinCode(joinCode)
                .members(new ArrayList<>(List.of(creatorTeam)))
                .build();

        return leagueRepository.save(league);
    }

    @Transactional
    public void joinLeague(User user, String joinCode) {
        League league = leagueRepository.findByJoinCode(joinCode)
                .orElseThrow(() -> new RuntimeException("INVALID_JOIN_CODE"));

        Team userTeam = teamRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("CREATE_TEAM_FIRST"));

        if (league.getMembers().contains(userTeam)) {
            throw new RuntimeException("ALREADY_A_MEMBER");
        }

        league.getMembers().add(userTeam);
        leagueRepository.save(league);
    }

    @Transactional
    public LeagueResponseDTO getLeagueStandings(Long leagueId) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new RuntimeException("LEAGUE_NOT_FOUND"));

        // Sort teams by points descending
        List<TeamStandingDTO> standings = league.getMembers().stream()
                .sorted((t1, t2) -> t2.getTotalPoints().compareTo(t1.getTotalPoints()))
                .map(team -> TeamStandingDTO.builder()
                        .teamId(team.getId())
                        .teamName(team.getTeamName())
                        .managerName(team.getUser().getUsername()) // Assumes User has username
                        .totalPoints(team.getTotalPoints())
                        .gwPoints(25) // Mock GW points for now
                        .build())
                .toList();

        return LeagueResponseDTO.builder()
                .id(league.getId())
                .name(league.getName())
                .joinCode(league.getJoinCode())
                .standings(standings)
                .build();
    }
}