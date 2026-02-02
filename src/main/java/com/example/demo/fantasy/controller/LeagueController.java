package com.example.demo.fantasy.controller;

import com.example.demo.fantasy.dto.LeagueResponseDTO;
import com.example.demo.fantasy.entity.League;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.entity.User;
import com.example.demo.fantasy.repository.LeagueRepository;
import com.example.demo.fantasy.repository.TeamRepository;
import com.example.demo.fantasy.service.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/leagues")
@RequiredArgsConstructor
public class LeagueController {
    private final LeagueService leagueService;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        League newLeague = leagueService.createLeague(user, request.get("name"));
        return ResponseEntity.ok(newLeague);
    }

    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        leagueService.joinLeague(user, request.get("code"));
        return ResponseEntity.ok(Map.of("message", "JOINED_SUCCESSFULLY"));
    }

    @GetMapping("/my-leagues")
    public ResponseEntity<List<League>> getMyLeagues(@AuthenticationPrincipal User user) {
        Team userTeam = teamRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("TEAM_NOT_FOUND"));
        return ResponseEntity.ok(leagueRepository.findAllByMembersId(userTeam.getId()));
    }

    @GetMapping("/{id}/standings")
    public ResponseEntity<LeagueResponseDTO> getStandings(@PathVariable Long id) {
        return ResponseEntity.ok(leagueService.getLeagueStandings(id));
    }
}