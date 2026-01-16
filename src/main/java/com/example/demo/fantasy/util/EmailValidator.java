package com.example.demo.fantasy.util;

import java.util.regex.Pattern;

public class EmailValidator {
    // The standard Regex for a valid email address
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);

    public static boolean isValid(String email) {
        if (email == null) return false;
        return pattern.matcher(email).matches();
    }

    public static String sanitize(String email) {
        if (email == null) return null;
        return email.trim().toLowerCase();
    }
}