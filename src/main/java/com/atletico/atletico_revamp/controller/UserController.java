package com.atletico.atletico_revamp.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atletico.atletico_revamp.dto.BaseResponse;
import com.atletico.atletico_revamp.dto.DataResponse;
import com.atletico.atletico_revamp.entities.User;
import com.atletico.atletico_revamp.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    // Create or Update a User
    @PostMapping
    public ResponseEntity<BaseResponse> createOrUpdateUser(@RequestBody User user) {
        try {
            // Save User
            userService.saveUser(user);
            log.info("User created or updated: {}", user.getUsername());

            // Build response
            BaseResponse response = new BaseResponse(
                true,
                "success",
                "User " + user.getUsername() + " successfully created or updated"
            );

            // Return response
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Log error
            log.error("Error creating or updating user", e);

            // Build response
            BaseResponse errorResponse = new BaseResponse(
                false,
                "fail",
                "User failed to be created or updated"
            );

            // Return response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataResponse<User>> getUserById(@PathVariable Long id) {
        try {
            // Try to get User
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                log.info("Getting user " + user.get().getUsername());

                // Build successfull response if User was retrieved
                DataResponse<User> response = new DataResponse<User>(
                    true,
                    "success",
                    "User successfully retrieved",
                    user
                );

                // Return User
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                throw new Error("Unable to find user with id " + id);
            }
        } catch (Exception e) {
            // Log error
            log.error("Error retrieving user", e);

            // Build failed data response
            DataResponse<User> errorResponse = new DataResponse<>(
                false, 
                "fail", 
                "Error retrieving user: " + e.getMessage(), 
                null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
    
}
