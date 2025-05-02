package com.coffee_backend.service;

import cn.hutool.core.bean.BeanUtil;
import com.coffee_backend.dto.*;
import com.coffee_backend.entity.User;
import com.coffee_backend.enumType.UserRole;
import com.coffee_backend.repo.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public ApiResponse saveFarmer(SaveFarmerRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        if (username == null || username.trim().isEmpty()) {
            return ApiResponse.error(400, "Username should not be null");
        }

        if (email == null || email.trim().isEmpty()) {
            return ApiResponse.error(400, "Email should not be null");
        }

        if (password == null || password.trim().isEmpty()) {
            return ApiResponse.error(400, "Password should not be null");
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ApiResponse.error(400, "Email format is null");
        }

        if (userRepository.existsByUsername(username)) {
            return ApiResponse.error(400, "Username already exist");
        }

        if (userRepository.existsByEmail(email)) {
            return ApiResponse.error(400, "Email already exist");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(UserRole.FARMER);

        User savedUser = userRepository.save(user);

        SaveFarmerResponse response = new SaveFarmerResponse();

        BeanUtils.copyProperties(savedUser, response);

        return ApiResponse.success(response);
    }
    
    public ApiResponse getAllFarmers() {
        // 查询所有农户用户
        List<User> farmers = userRepository.findByRole(UserRole.FARMER);
        
        // 将实体对象转换为DTO
        List<UserDTO> farmerDTOs = farmers.stream()
            .map(farmer -> {
                UserDTO dto = new UserDTO();
                BeanUtils.copyProperties(farmer, dto);
                return dto;
            })
            .collect(Collectors.toList());
        
        return ApiResponse.success(farmerDTOs);
    }

    public ApiResponse queryCustomer() {
        List<User> customers = userRepository.findByRole(UserRole.CUSTOMER);

        List<UserDTO> userDTOS = customers.stream().map(customer -> BeanUtil.copyProperties(customer, UserDTO.class)).toList();

        QueryNumberResponse response = QueryNumberResponse.builder()
                .nums(userDTOS.size())
                .users(userDTOS)
                .build();

        return ApiResponse.success(response);
    }
}
