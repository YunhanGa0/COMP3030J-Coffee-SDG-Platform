package com.coffee_backend.security;

import com.coffee_backend.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;

@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) {

        log.warn("未登录或 Token 无效：{}", authException.getMessage());

        // ① 状态码
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401
        response.setContentType("application/json;charset=UTF-8");

        // ② JSON 体
        ApiResponse body = ApiResponse.error(401, "未登录或 Token 已过期");
        try (PrintWriter out = response.getWriter()) {
            out.print(MAPPER.writeValueAsString(body));
            out.flush();
        } catch (Exception ignore) {}
    }
}
