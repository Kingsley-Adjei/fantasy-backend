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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/leagues")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class LeagueController {
    private final LeagueService leagueService;
    private final LeagueRepository leagueRepository;
    private final TeamRepository teamRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        League newLeague = leagueService.createLeague(user, request.get("name"));
        return ResponseEntity.ok(Map.of(
                "id", newLeague.getId(),
                "name", newLeague.getName(),
                "joinCode", newLeague.getJoinCode()
        ));
    }


    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody Map<String, String> request, @AuthenticationPrincipal User user) {
        leagueService.joinLeague(user, request.get("code"));
        return ResponseEntity.ok(Map.of("message", "JOINED_SUCCESSFULLY"));
    }

    @GetMapping("/my-leagues")
    public ResponseEntity<?> getMyLeagues(@AuthenticationPrincipal User user) {
        // TEMPORARY DEBUG
        System.out.println("USER FROM TOKEN: " + user);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        Optional<Team> userTeam = teamRepository.findByUserId(user.getId());

        if (userTeam.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        List<League> userLeagues = leagueRepository.findAllByMembersId(userTeam.get().getId());
        List<Map<String, Object>> response = userLeagues.stream()
                .map(l -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", l.getId());
                    map.put("name", l.getName());
                    map.put("joinCode", l.getJoinCode());
                    return map;
                })
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/standings")
    public ResponseEntity<LeagueResponseDTO> getStandings(
            @PathVariable Long id,
            @AuthenticationPrincipal User user) {  // ADD user param
        return ResponseEntity.ok(leagueService.getLeagueStandings(id, user));
    }
}