package com.example.demo.fantasy.controller;

import com.example.demo.fantasy.dto.LineupRequest;
import com.example.demo.fantasy.dto.TeamRequest;
import com.example.demo.fantasy.dto.TransferRequest;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.entity.User;
import com.example.demo.fantasy.repository.TeamRepository;
import com.example.demo.fantasy.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor // This fixes the 'Cannot resolve teamService' error
public class TeamController {

    private final TeamService teamService;
    @PostMapping("/create")
    public ResponseEntity<?> createTeam(
            @RequestBody TeamRequest request,
            @AuthenticationPrincipal User user) {
        try {
            Team newTeam = teamService.createInitialSquad(user, request.getTeamName(), request.getPlayerIds());
            return ResponseEntity.ok(Map.of(
                    "message", "TEAM_CREATED_SUCCESSFULLY",
                    "teamId", newTeam.getId()
            ));
        } catch (RuntimeException e) {
            if ("USER_ALREADY_HAS_TEAM".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "USER_ALREADY_HAS_TEAM"));
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }
    @GetMapping("/my-team")
    public ResponseEntity<?> getMyTeam(@AuthenticationPrincipal User user) {
        // We use the 'user' object injected by Spring Security via the token
        return teamService.getTeamByUser(user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> transferPlayer(
            @RequestBody TransferRequest request,
            @AuthenticationPrincipal User user) {

        teamService.performTransfer(user, request);

        return ResponseEntity.ok(java.util.Map.of(
                "message", "TRANSFER_SUCCESSFUL",
                "status", "SUCCESS"
        ));
    }
    @PostMapping("/update-lineup")
    public ResponseEntity<?> updateLineup(
            @RequestBody LineupRequest request,
            @AuthenticationPrincipal User user) {

        teamService.updateLineup(user, request);

        return ResponseEntity.ok(java.util.Map.of(
                "message", "Lineup updated successfully!"
        ));
    }
    @GetMapping("/has-team")
    public ResponseEntity<Map<String, Boolean>> hasTeam(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of("hasTeam", teamService.hasTeam(user)));
    }
}