package com.example.demo.fantasy.controller;

import com.example.demo.fantasy.dto.TeamRequest;
import com.example.demo.fantasy.entity.Team;
import com.example.demo.fantasy.entity.User;
import com.example.demo.fantasy.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor // This fixes the 'Cannot resolve teamService' error
public class TeamController {

    private final TeamService teamService; // Must be 'private final' for Lombok to work

    @PostMapping("/create")
    public ResponseEntity<?> createTeam(
            @RequestBody TeamRequest request,
            @AuthenticationPrincipal User user) {

        // This calls the logic that checks the 15 players and 100 Cedis budget
        Team newTeam = teamService.createInitialSquad(user, request.getTeamName(), request.getPlayerIds());

        return ResponseEntity.ok(Map.of(
                "message", "TEAM_CREATED_SUCCESSFULLY",
                "teamId", newTeam.getId()
        ));
    }
}