package com.example.demo.fantasy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final StringRedisTemplate redisTemplate;

    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Save to Redis: Key is email, Value is OTP
        // It will AUTOMATICALLY delete after 5 minutes
        redisTemplate.opsForValue().set(email, otp, 5, TimeUnit.MINUTES);

        return otp;
    }

    public boolean validateOtp(String email, String userOtp) {
        String cachedOtp = redisTemplate.opsForValue().get(email);
        return userOtp.equals(cachedOtp);
    }
}