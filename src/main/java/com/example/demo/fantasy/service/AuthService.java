package com.example.demo.fantasy.service;

import com.example.demo.fantasy.entity.User;
import com.example.demo.fantasy.repository.UserRepository;
import com.example.demo.fantasy.util.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor// Automatically injects the UserRepository
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Injected from our Config

    // private final OtpService otpService; // We will build this next!

    public String initiateHybridAuth(String rawEmail, String password) {
        String email = EmailValidator.sanitize(rawEmail);
        if (!EmailValidator.isValid(email)) {
            throw new RuntimeException("Invalid email format provided.");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            // LOGIN CASE: Check if the password matches the hash in DB
            boolean matches = passwordEncoder.matches(password, existingUser.get().getPassword());
            if (!matches) {
                throw new RuntimeException("Invalid credentials");
            }
            return "EXISTING_USER_PROCEED_TO_OTP";
        } else {
            // LOGIC FOR NEW USER (SIGNUP)
            String hashedPassword = passwordEncoder.encode(password);

            User newUser = User.builder()
                    .email(email)
                    .password(hashedPassword)
                    .is_verified(false)
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(newUser);
            // 3. Trigger OTP for Registration
            return "NEW_USER_OTP_SENT";
        }
    }
}
