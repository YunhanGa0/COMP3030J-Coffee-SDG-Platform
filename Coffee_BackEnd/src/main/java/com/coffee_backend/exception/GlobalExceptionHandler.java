package com.coffee_backend.exception;

import com.coffee_backend.dto.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ApiResponse handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        String msg = request.getRequestURI().startsWith("/api/orders")
                ? "Only customers can place orders."
                : "You don't have permission to access this resource.";
        return ApiResponse.error(403, msg);
    }


    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException e){
        log.error(e.toString(), e);
        return ApiResponse.error(500, "Server Error");
    }

    /* ---------- 业务异常 ---------- */

    @ExceptionHandler(NotFoundException.class)
    public ApiResponse handleNotFound(NotFoundException ex) {
        return ApiResponse.error(404, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ApiResponse handleForbidden(ForbiddenException ex) {
        return ApiResponse.error(403, ex.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    public ApiResponse handleBadRequest(BadRequestException ex) {
        return ApiResponse.error(400, ex.getMessage());
    }
}
