package com.coffee_backend.service;

import com.coffee_backend.dto.ApiResponse;
import com.coffee_backend.dto.LoginRequest;
import com.coffee_backend.dto.LoginResponse;
import com.coffee_backend.dto.UserDTO;
import com.coffee_backend.dto.UserRegisterRequest;
import com.coffee_backend.entity.User;
import com.coffee_backend.enumType.UserRole;
import com.coffee_backend.repo.UserRepository;
import com.coffee_backend.util.JwtUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public ApiResponse register(UserRegisterRequest request) {
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
        // TODO 默认设置吗？ 还是有选项？？
        user.setRole(UserRole.CUSTOMER);

        User savedUser = userRepository.save(user);

        UserDTO userDTO = new UserDTO();

        BeanUtils.copyProperties(savedUser, userDTO);

        return ApiResponse.success(userDTO);
    }

    public ApiResponse createAdminForTest() {
        String adminUsername = "admin";
        String adminPassword = "admin123";
        String adminEmail = "admin@example.com";
        
        // 检查管理员是否已存在
        if (!userRepository.existsByUsername(adminUsername)) {
            // 创建管理员账户
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
        }
        
        // 获取管理员账户
        User admin = userRepository.findByUsername(adminUsername).get();
        
        // 生成令牌
        String token = jwtUtil.generateToken(admin);
        
        // 创建登录响应
        LoginResponse response = LoginResponse.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .email(admin.getEmail())
                .role(admin.getRole())
                .token(token)
                .build();
        
        return ApiResponse.success(response);
    }

    public ApiResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // 参数验证
        if (username == null || username.trim().isEmpty()) {
            return ApiResponse.error(400, "用户名不能为空");
        }

        if (password == null || password.trim().isEmpty()) {
            return ApiResponse.error(400, "密码不能为空");
        }

        // 查找用户
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return ApiResponse.error(400, "User not exist");
        }

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return ApiResponse.error(400, "Password error");
        }

        // 生成token
        String token = jwtUtil.generateToken(user);

        // 构建响应
        LoginResponse response = LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();

        return ApiResponse.success(response);
    }
}
