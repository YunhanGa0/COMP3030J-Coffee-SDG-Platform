package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.LoginRequest;
import com.coffee_backend.dto.UserRegisterRequest;
import com.coffee_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 * Handles user authentication operations including login, registration,
 * and test endpoints for development purposes
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * User login endpoint
     * 
     * @param loginRequest Login credentials (username and password)
     * @return Authentication result including JWT token
     */
    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    /**
     * User registration endpoint
     * 
     * @param userRegisterRequest User registration information
     * @return Registration result
     */
    @PostMapping("/register")
    public ApiResponse register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return authService.register(userRegisterRequest);
    }

    /**
     * Testing endpoint that creates an admin account and returns a token
     * For development and testing purposes only
     * 
     * @return Admin authentication token
     */
    @GetMapping("/admin/test")
    public ApiResponse getAdminTokenForTest() {
        return authService.createAdminForTest();
    }
}
