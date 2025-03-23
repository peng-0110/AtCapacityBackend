package com.example.AtCapacity.service;

import com.example.AtCapacity.model.User;
import com.example.AtCapacity.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
        User user = new User(username, password, "USER");
        return userRepository.save(user);
    }

    public boolean authenticateUser(String username, String password) {
        return userRepository.findByUsername(username)
            .map(user -> passwordEncoder.matches(password, user.getPassword()))
            .orElse(false);
    }
}