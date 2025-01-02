package com.atletico.atletico_revamp.controller;

import com.atletico.atletico_revamp.config.JwtConfigurationProperties;
import com.atletico.atletico_revamp.dto.LoginRequest;
import com.atletico.atletico_revamp.dto.LoginResponse;
import com.atletico.atletico_revamp.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    JwtConfigurationProperties jwtConfigurationProperties;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authenticationService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/testing")
    public ResponseEntity<?> testing() {
        return ResponseEntity.ok(jwtConfigurationProperties);
    }

}
