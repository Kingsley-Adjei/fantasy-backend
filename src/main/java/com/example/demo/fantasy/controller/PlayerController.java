package com.example.demo.fantasy.controller;

import com.example.demo.fantasy.entity.Player;
import com.example.demo.fantasy.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows your Expo app to connect
public class PlayerController {

    private final PlayerRepository playerRepository;

    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        return ResponseEntity.ok(playerRepository.findAll());
    }

    @GetMapping("/position/{pos}")
    public ResponseEntity<List<Player>> getByPosition(@PathVariable String pos) {
        return ResponseEntity.ok(playerRepository.findByPosition(pos.toUpperCase()));
    }
}