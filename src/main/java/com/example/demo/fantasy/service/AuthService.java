package com.example.demo.fantasy.service;

import com.example.demo.fantasy.entity.User;
import com.example.demo.fantasy.repository.UserRepository;
import com.example.demo.fantasy.util.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor// Automatically injects the UserRepository
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injected from our Config

    private final EmailService emailService;
    private final OtpService otpService;
    private final JwtService jwtService;

    public Map<String, String> initiateHybridAuth(String rawEmail, String password) {
        String email = EmailValidator.sanitize(rawEmail);
        if (!EmailValidator.isValid(email)) {
            throw new RuntimeException("Invalid email format provided.");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            boolean matches = passwordEncoder.matches(password, user.getPassword());

            if (!matches) {
                throw new RuntimeException("Invalid credentials");
            }

            if (!user.is_verified()) {
                String otp = otpService.generateOtp(email);
                emailService.sendOtpEmail(email, otp);
                return Map.of("message", "NEW_USER_OTP_SENT"); // Wrapped in Map
            }

            // --- SUCCESS CASE ---
            // We generate the token here because the user is verified!
            String token = jwtService.generateToken(email);
            return Map.of(
                    "message", "LOGIN_SUCCESS",
                    "token", token
            );

        } else {
            String hashedPassword = passwordEncoder.encode(password);
            User newUser = User.builder()
                    .email(email)
                    .password(hashedPassword)
                    .is_verified(false) // User is not verified yet
                    .build();

            userRepository.save(newUser);

            // SEND OTP ONLY FOR NEW USERS
            String otp = otpService.generateOtp(email);
            try {
                emailService.sendOtpEmail(email, otp);
            } catch (Exception e) {
                throw new RuntimeException("User created but failed to send verification email.");
            }

            return Map.of("message", "NEW_USER_OTP_SENT");
        }
    }
    public Map<String, String> verifyOtp(String email, String userOtp) {
        String sanitizedEmail = EmailValidator.sanitize(email);

        // 1. Check if the OTP matches what is in Redis
        boolean isValid = otpService.validateOtp(sanitizedEmail, userOtp);

        if (isValid) {
            // 2. Find the user in Postgres
            User user = userRepository.findByEmail(sanitizedEmail)
                    .orElseThrow(() -> new RuntimeException("User not found after verification"));

            // 3. Mark them as verified and save
            user.set_verified(true);
            userRepository.save(user);

            // 4. Since they are now verified, give them their "ID Card" (JWT)
            String token = jwtService.generateToken(sanitizedEmail);

            return Map.of(
                    "message", "VERIFICATION_SUCCESSFUL",
                    "token", token
            );
        } else {
            // This happens if the code is wrong OR if it expired in Redis
            throw new RuntimeException("Invalid or expired verification code.");
        }
    }
}
