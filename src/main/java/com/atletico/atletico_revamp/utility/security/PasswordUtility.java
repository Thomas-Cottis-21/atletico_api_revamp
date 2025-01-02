package com.atletico.atletico_revamp.utility.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
     *
     * @param password The plain text password to be hashed
     * @return The hashed password
     */
    public String hashPassword(String password) {
        return ENCODER.encode(password);
    }

    /**
     * Verifies if the plain text password matches the hashed password
     *
     * @param plainTextPassword The password typed by the user
     * @param hashedPassword The hashed password stored in the database
     * @return True if the passwords match; false otherwise
     */
    public static boolean verifyPassword(String plainTextPassword, String hashedPassword) {
        return ENCODER.matches(plainTextPassword, hashedPassword);
    }
}
