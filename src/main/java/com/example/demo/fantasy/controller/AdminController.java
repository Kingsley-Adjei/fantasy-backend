package com.example.demo.fantasy.controller;

import com.example.demo.fantasy.service.GameweekService;
import com.example.demo.fantasy.service.PointsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final PointsService pointsService;
    private final GameweekService gameweekService;

    // In a real app, you'd protect this with @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update-points")
    public ResponseEntity<?> updatePoints(@RequestBody Map<String, Object> request) {
        Long playerId = Long.valueOf(request.get("playerId").toString());
        Integer points = Integer.valueOf(request.get("points").toString());
        pointsService.updatePlayerPoints(playerId, points);
        return ResponseEntity.ok(Map.of("message", "POINTS_UPDATED_FOR_ALL_TEAMS"));
    }

    // 2. New: Process the WHOLE week (The Leaderboard Button)
    @PostMapping("/process-week")
    public ResponseEntity<?> processWeek() {
        gameweekService.finalizeGameweek();
        return ResponseEntity.ok(Map.of("message", "GAMEWEEK_FINALISED_LEADERBOARD_UPDATED"));
    }
}
