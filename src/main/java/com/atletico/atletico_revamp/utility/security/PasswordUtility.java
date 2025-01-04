package com.atletico.atletico_revamp.utility.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public final class PasswordUtility {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * Private constructor to prevent instantiation
     */
    private PasswordUtility() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Hashes the given password using BCrypt
     * Used to hash passwords before storing in database
     *
     * @param password The plain text password to be hashed
     * @return The hashed password
     */
    public static String hashPassword(String password) {
        return ENCODER.encode(password);
    }

    public static PasswordEncoder getPasswordEncoder() {
        return ENCODER;
    }
}
