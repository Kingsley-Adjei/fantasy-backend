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
            // 'result' is already a Map like {"message": "NEW_USER_OTP_SENT"}
            Map<String, String> result = authService.initiateHybridAuth(email, password);

            // Return result directly!
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        try {
            // This should return the Map with "message" and "token"
            Map<String, String> result = authService.verifyOtp(email, otp);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}