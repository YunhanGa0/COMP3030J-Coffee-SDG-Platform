package com.coffee_backend.exception;

import com.coffee_backend.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse handleRuntimeException(RuntimeException e){
        log.error(e.toString(), e);
        return ApiResponse.error(500, "Server error");
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
