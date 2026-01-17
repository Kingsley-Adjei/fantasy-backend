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

    private final EmailService emailService;
    private final OtpService otpService;

    public String initiateHybridAuth(String rawEmail, String password) {
        String email = EmailValidator.sanitize(rawEmail);
        if (!EmailValidator.isValid(email)) {
            throw new RuntimeException("Invalid email format provided.");
        }

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            boolean matches = passwordEncoder.matches(password, existingUser.get().getPassword());
            if (!matches) {
                throw new RuntimeException("Invalid credentials");
            }
            if (!user.is_verified()) {
                // They exist but aren't verified! Send them an OTP now.
                String otp = otpService.generateOtp(email);
                emailService.sendOtpEmail(email, otp);
                return "NEW_USER_OTP_SENT";
            }
            return "LOGIN_SUCCESS";

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

            return "NEW_USER_OTP_SENT";
        }
    }
}
