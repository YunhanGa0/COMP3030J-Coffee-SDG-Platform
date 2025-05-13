package com.coffee_backend.controller;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.LoginRequest;
import com.coffee_backend.dto.UserRegisterRequest;
import com.coffee_backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Authentication", description = "APIs for user authentication including login and registration")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * User login endpoint
     * 
     * @param loginRequest Login credentials (username and password)
     * @return Authentication result including JWT token
     */
    @Operation(summary = "User login", description = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Authentication successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Authentication failed - Invalid credentials")
    })
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
    @Operation(summary = "User registration", description = "Registers a new user account")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Registration successful"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Registration failed - Invalid data or username already exists")
    })
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
    @Operation(summary = "Get admin test token", description = "Creates an admin account and returns a token. For development purposes only.")
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Admin token generated successfully")
    })
    @GetMapping("/admin/test")
    public ApiResponse getAdminTokenForTest() {
        return authService.createAdminForTest();
    }

}
