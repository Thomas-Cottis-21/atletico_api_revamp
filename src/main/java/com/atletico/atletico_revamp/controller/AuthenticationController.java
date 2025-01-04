package com.atletico.atletico_revamp.controller;

import com.atletico.atletico_revamp.config.JwtConfigurationProperties;
import com.atletico.atletico_revamp.dto.LoginRequest;
import com.atletico.atletico_revamp.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    JwtConfigurationProperties jwtConfigurationProperties;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    @Autowired
    public AuthenticationController(JwtConfigurationProperties jwtConfigurationProperties, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.jwtConfigurationProperties = jwtConfigurationProperties;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok(this.jwtService.generateToken(loginRequest.getUsername()));
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    @GetMapping("/testing")
    public ResponseEntity<?> testing() {
        return ResponseEntity.ok(jwtConfigurationProperties);
    }

}
