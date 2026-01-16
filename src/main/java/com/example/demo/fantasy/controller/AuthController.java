package com.example.demo.fantasy.controller;

import com.example.demo.fantasy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows your mobile app to talk to the backend
public class AuthController {

    private final AuthService authService;

    @PostMapping("/initiate")
    public ResponseEntity<?> initiateAuth(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");

        try {
            String result = authService.initiateHybridAuth(email, password);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}