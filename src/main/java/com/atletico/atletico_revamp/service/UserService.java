package com.atletico.atletico_revamp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atletico.atletico_revamp.entities.User;
import com.atletico.atletico_revamp.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Create or Update a User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Get a User by ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    // Get all Users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Delete a User by ID
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Next is to create methods to handle user passwords (hashing / updating)
}
