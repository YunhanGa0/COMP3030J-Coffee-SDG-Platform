package com.coffee_backend.security;

import com.coffee_backend.dto.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Slf4j
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        log.warn("未登录或 Token 无效：{}", authException.getMessage());

        //
        ApiResponse api = ApiResponse.error(401, "Login required");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        //

        MAPPER.writeValue(response.getWriter(), api);

    }
}
