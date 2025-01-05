package com.atletico.atletico_revamp.controller;

import java.util.Optional;

import com.atletico.atletico_revamp.dto.ApiError;
import com.atletico.atletico_revamp.dto.Metadata;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atletico.atletico_revamp.dto.ApiResponse;
import com.atletico.atletico_revamp.entities.User;
import com.atletico.atletico_revamp.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create or Update a User
    @PostMapping
    public ResponseEntity<ApiResponse<User>> createOrUpdateUser(@RequestBody User user, HttpServletRequest request) {
        try {
            // Save User
            userService.saveUser(user);
            log.info("User created or updated: {}", user.getUsername());

            // Build response
            ApiResponse<User> response = new ApiResponse<>(
                true,
                "success",
                "User " + user.getUsername() + " successfully created or updated",
                user,
                null
            );

            // Return response
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            // Log error
            log.error("Error creating or updating user", e);

            // Build response
            ApiResponse<User> errorResponse = new ApiResponse<>(
                false,
                "fail",
                "User failed to be created or updated",
                user,
                new ApiError(
                    500,
                    e.getMessage(),
                    new Metadata(request.getRequestURI())
                )
            );

            // Return response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // Get User by id
    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id, HttpServletRequest request) {
        log.info("Attempting to get user {}", id);
        try {
            // Try to get User
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                log.info("Getting user {}", user.get().getUsername());

                // Build successful response if User was retrieved
                ApiResponse<User> response = new ApiResponse<>(
                    true,
                    "success",
                    "User successfully retrieved",
                    user.get(),
                    null
                );

                // Return User
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } 

            log.warn("Attempting to get non existent user with id {}", id);

            ApiResponse<User> response = new ApiResponse<>(
                false,
                "fail",  
                "Unable to find user of id " + id, 
                null,
                new ApiError(
                    404,
                    "Unable to find user of id " + id,
                    new Metadata(request.getRequestURI())
                )
            );

            // Return error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Log error
            log.error("Error retrieving user of id {}", id, e);

            // Build failed data response
            ApiResponse<User> errorResponse = new ApiResponse<>(
                false, 
                "fail", 
                "Error retrieving user: " + e.getMessage(), 
                null,
                new ApiError(
                    500,
                    e.getMessage(),
                    new Metadata(request.getRequestURI())
                )
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Delete User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUserById(@PathVariable Long id, HttpServletRequest request) {
        try {
            // Check if user exists before attempting to delete user
            Optional<User> user = userService.getUserById(id);
            if (user.isEmpty()) {

                log.warn("Attempted to delete non-existent user with id {}", id);

                // Build not found response
                ApiResponse<Void> response = new ApiResponse<>(
                    false,
                    "fail",
                    "User with id " + id + " does not exist",
                    null,
                    new ApiError(
                        404,
                        "User with id " + id + " does not exist",
                        new Metadata(request.getRequestURI())
                    )
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Delete user if found
            userService.deleteUser(id);
            log.info("Deleted user with id {}", id);

            // Build success response
            ApiResponse<Void> response = new ApiResponse<>(
                true,
                "success",
                "User with id " + id + " successfully deleted",
                null,
                null
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            // Log error
            log.error("Error deleting user with id {}, {}", id, e.getMessage(), e);

            ApiResponse<Void> errorResponse = new ApiResponse<>(
                false,
                "fail",
                "Error deleting user with id " + id + ": " + e.getMessage(),
                null,
                new ApiError(
                    500,
                    e.getMessage(),
                    new Metadata(request.getRequestURI())
                )
            );

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
