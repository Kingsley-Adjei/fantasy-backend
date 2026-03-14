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

import java.util.*;

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
                .createdBy(user)
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
        if (league.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("You are already the admin of this league.");
        }


        league.getMembers().add(userTeam);
        leagueRepository.save(league);
    }

    @Transactional
    public LeagueResponseDTO getLeagueStandings(Long leagueId, User currentUser) {
        League league = leagueRepository.findById(leagueId)
                .orElseThrow(() -> new RuntimeException("LEAGUE_NOT_FOUND"));

        // Sort teams by points descending
        List<TeamStandingDTO> standings = league.getMembers().stream()
                .sorted((t1, t2) -> t2.getTotalPoints().compareTo(t1.getTotalPoints()))
                .map(team -> TeamStandingDTO.builder()
                        .id(team.getId())
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
                .isAdmin(league.getCreatedBy() != null &&
                        league.getCreatedBy().getId().equals(currentUser.getId())) // ADD
                .standings(standings)
                .build();
    }
    @Transactional
    public void autoJoinGlobalLeague(Team team) {
        // Find or create the single global league
        League globalLeague = leagueRepository.findByIsGlobalTrue()
                .orElseGet(() -> {
                    League newGlobal = League.builder()
                            .name("Global League")
                            .isGlobal(true)
                            .joinCode("GLOBAL")
                            .build();
                    return leagueRepository.save(newGlobal);
                });

        // Only add if not already a member
        boolean alreadyMember = globalLeague.getMembers()
                .stream().anyMatch(m -> m.getId().equals(team.getId()));

        if (!alreadyMember) {
            globalLeague.getMembers().add(team);
            leagueRepository.save(globalLeague);
        }
    }
    public List<Map<String, Object>> getMyLeagues(User user) {
        Team team = teamRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Team not found"));

        List<League> leagues = leagueRepository.findAllByMembersId(team.getId());

        return leagues.stream().map(l -> {
            // Get this team's rank in the league
            List<Team> members = l.getMembers();
            members.sort((a, b) -> Integer.compare(b.getTotalPoints(), a.getTotalPoints()));
            int rank = members.stream()
                    .map(Team::getId)
                    .collect(java.util.stream.Collectors.toList())
                    .indexOf(team.getId()) + 1;

            Map<String, Object> map = new HashMap<>();
            map.put("id", l.getId());
            map.put("name", l.getName());
            map.put("joinCode", l.getJoinCode());
            map.put("isGlobal", l.isGlobal());
            map.put("isAdmin", l.getCreatedBy() != null &&
                    l.getCreatedBy().getId().equals(user.getId()));            map.put("currentRank", rank);
            map.put("lastRank", rank); // TODO: store previous rank in a separate field later
            map.put("memberCount", members.size());
            return map;
        }).toList();
    }
}