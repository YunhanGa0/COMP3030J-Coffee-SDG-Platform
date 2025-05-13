package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.*;
import com.coffee_backend.entity.User;
import com.coffee_backend.enumType.UserRole;
import com.coffee_backend.exception.BadRequestException;
import com.coffee_backend.exception.ForbiddenException;
import com.coffee_backend.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Service for administrator operations
 */
@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    /**
     * Create a new farmer account
     *
     * @param request farmer registration data
     * @return API response with created farmer data
     */
    public ApiResponse saveFarmer(SaveFarmerRequest request) {
        validateFarmerRequest(request);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.FARMER);

        User savedUser = userRepository.save(user);

        SaveFarmerResponse response = BeanUtil.copyProperties(savedUser, SaveFarmerResponse.class);

        return ApiResponse.success(response);
    }
    
    /**
     * Get all farmers in the system
     *
     * @return API response with list of farmers
     */
    public ApiResponse getAllFarmers() {
        List<User> farmers = userRepository.findByRole(UserRole.FARMER);
        
        List<UserDTO> farmerDTOs = farmers.stream()
            .map(farmer -> BeanUtil.copyProperties(farmer, UserDTO.class))
            .toList();
        
        return ApiResponse.success(farmerDTOs);
    }

    /**
     * Query all customers and their count
     *
     * @return API response with customer data and count
     */
    public ApiResponse queryCustomer() {
        List<User> customers = userRepository.findByRole(UserRole.CUSTOMER);

        List<UserDTO> userDTOs = customers.stream()
            .map(customer -> BeanUtil.copyProperties(customer, UserDTO.class))
            .toList();

        QueryNumberResponse response = QueryNumberResponse.builder()
                .nums(userDTOs.size())
                .users(userDTOs)
                .build();

        return ApiResponse.success(response);
    }

    /**
     * Validate farmer registration request
     *
     * @param request farmer registration data to validate
     * @throws BadRequestException if validation fails
     * @throws ForbiddenException if username or email already exists
     */
    private void validateFarmerRequest(SaveFarmerRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        if (username == null || username.trim().isEmpty()) {
            throw new BadRequestException("Username should not be null");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new BadRequestException("Email should not be null");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new BadRequestException("Password should not be null");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException("Email format is invalid");
        }

        if (userRepository.existsByUsername(username)) {
            throw new ForbiddenException("Username already exists");
        }

        if (userRepository.existsByEmail(email)) {
            throw new ForbiddenException("Email already exists");
        }
    }
}
