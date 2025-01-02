package com.atletico.atletico_revamp.service;

import com.atletico.atletico_revamp.entities.User;
import com.atletico.atletico_revamp.exceptions.AuthenticationException;
import com.atletico.atletico_revamp.repository.UserRepository;
import com.atletico.atletico_revamp.utility.security.JwtUtility;
import com.atletico.atletico_revamp.utility.security.PasswordUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtility jwtUtility;

    /**
     * Authenticates or denies a user
     * If authenticated, returns a jwt; otherwise throws an exception
     * @param username Username typed by the user
     * @param password Password typed by the user
     * @return A jwt when authenticated
     */
    public String authenticate(String username, String password) {
        // Retrieve user from database by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));

        // Verify the password
        if (!PasswordUtility.verifyPassword(password, user.getPassword())) {
            throw new AuthenticationException("Invalid username or password");
        }

        // When roles are introduced, add them to the token here
        return jwtUtility.createToken(username);
    }
}
