package com.coffee_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper for all endpoints
 * Provides consistent response format across the application
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response format returned by all endpoints")
public class ApiResponse {
    
    @Schema(description = "Response status code (200: success, 201: created, 4xx: client error, 5xx: server error)", example = "200")
    private Integer code;
    
    @Schema(description = "Response message describing the result", example = "Success")
    private String message;
    
    @Schema(description = "Response payload data, can be any type or null")
    private Object data;

    /**
     * Creates a success response with data
     * 
     * @param data Response payload
     * @return ApiResponse with code 200
     */
    public static ApiResponse success(Object data) {
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .data(data)
                .build();
    }

    /**
     * Creates a success response without data
     * 
     * @return ApiResponse with code 200
     */
    public static ApiResponse success() {
        return ApiResponse.builder()
                .code(200)
                .message("Success")
                .build();
    }

    /**
     * Creates a resource created response with data
     * Used for POST operations that create new resources
     * 
     * @param data Created resource data
     * @return ApiResponse with code 201
     */
    public static ApiResponse created(Object data) {
        return ApiResponse.builder()
                .code(201)
                .message("Success created")
                .data(data)
                .build();
    }

    /**
     * Creates a success response with custom message and data
     * 
     * @param message Custom success message
     * @param data Response payload
     * @return ApiResponse with code 200
     */
    public static ApiResponse success(String message, Object data) {
        return ApiResponse.builder()
                .code(200)
                .message(message)
                .data(data)
                .build();
    }

    /**
     * Creates an error response with custom code and message
     * 
     * @param code HTTP status code (usually 4xx or 5xx)
     * @param message Error description message
     * @return ApiResponse with error details
     */
    public static ApiResponse error(Integer code, String message) {
        return ApiResponse.builder()
                .code(code)
                .message(message)
                .build();
    }
}
