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

    // Get User by id
    @GetMapping("/get/{id}")
    public ResponseEntity<DataResponse<User>> getUserById(@PathVariable Long id) {
        log.info("Attempting to get user {}", id);
        try {
            // Try to get User
            Optional<User> user = userService.getUserById(id);
            if (user.isPresent()) {
                log.info("Getting user {}", user.get().getUsername());

                // Build successful response if User was retrieved
                DataResponse<User> response = new DataResponse<>(
                    true,
                    "success",
                    "User successfully retrieved",
                    user.get()
                );

                // Return User
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } 

            log.warn("Attempting to get non existent user with id {}", id);

            DataResponse<User> response = new DataResponse<>(
                false,
                "fail",  
                "Unable to find user of id " + id, 
                null
            );

            // Return error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Log error
            log.error("Error retrieving user of id {}", id, e);

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

    // Delete User
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<BaseResponse> deleteUserById(@PathVariable Long id) {
        try {
            // Check if user exists before attempting to delete user
            Optional<User> user = userService.getUserById(id);
            if (user.isEmpty()) {

                log.warn("Attempted to delete non-existent user with id {}", id);

                // Build not found response
                BaseResponse response = new BaseResponse(
                    false,
                    "fail",
                    "User with id " + id + " does not exist"
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Delete user if found
            userService.deleteUser(id);
            log.info("Deleted user with id {}", id);

            // Build success response
            BaseResponse response = new BaseResponse(
                true,
                "success",
                "User with id " + id + " successfully deleted"
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (Exception e) {
            // Log error
            log.error("Error deleting user with id {}, {}", id, e.getMessage(), e);

            BaseResponse errorResponse = new BaseResponse(
                false,
                "fail",
                "Error deleting user with id " + id + ": " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
