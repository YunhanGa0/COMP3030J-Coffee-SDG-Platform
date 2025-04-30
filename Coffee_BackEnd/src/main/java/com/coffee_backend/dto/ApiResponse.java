package com.coffee_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse {
    private Integer code;
    private String message;
    private Object data;

    public static ApiResponse success(Object data) {
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    public static ApiResponse success() {
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .build();
    }

    // Used to POST
    public static ApiResponse created(Object data) {
        return ApiResponse.builder()
                .code(201)
                .message("Success created")
                .data(data)
                .build();
    }

    public static ApiResponse success(String message, Object data) {
        return ApiResponse.builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    public static ApiResponse error(Integer code, String message) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
    }


}
