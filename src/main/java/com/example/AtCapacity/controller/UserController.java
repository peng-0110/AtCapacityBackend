package com.example.AtCapacity.controller;

import com.example.AtCapacity.model.User;
import com.example.AtCapacity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user authentication.
 * Provides endpoints for user registration and login.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user in the system.
     * @param request registration details containing username and password
     * @return 200 OK if successful, 400 Bad Request if username exists
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request.getUsername(), request.getPassword());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Authenticates a user's credentials.
     * @param request login credentials containing username and password
     * @return 200 OK if authenticated, 400 Bad Request if invalid credentials
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean isAuthenticated = userService.authenticateUser(
            request.getUsername(), 
            request.getPassword()
        );
        
        if (isAuthenticated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Invalid credentials");
        }
    }
}

/**
 * Data transfer object for user registration requests.
 */
class RegisterRequest {
    private String username;
    private String password;
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}

/**
 * Data transfer object for user login requests.
 */
class LoginRequest {
    private String username;
    private String password;
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}