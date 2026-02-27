package com.dontwait.server.exception;

import java.nio.file.AccessDeniedException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dontwait.server.dto.response.ApiResponse;
import com.dontwait.server.enums.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    //Handler error we dont think it happen
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(RuntimeException e) {
        log.error("Exception: ", e);

        ApiResponse apiResponse = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED.getCode())
                .message(ErrorCode.UNCATEGORIZED.getMessage())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException e) {
        log.error("AppException: ", e);
        ApiResponse apiResponse = ApiResponse.builder()
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();
        return ResponseEntity
                .status(e.getErrorCode().getHttpStatus())
                .body(apiResponse);
    }

    //Kiem tra quyen cua nguoi dung co duoc phep truy cap vao resource hay khong
    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException: ", e);
        return ResponseEntity
                .status(ErrorCode.UNAUTHORIZED.getHttpStatus())
                .body(ApiResponse.builder()
                    .code(ErrorCode.UNAUTHORIZED.getCode())
                    .message(ErrorCode.UNAUTHORIZED.getMessage())
                .build());
    }

    //Ham nay minh bat loi validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_ID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        }
        catch (IllegalArgumentException illegalArgumentException) {

        }
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(apiResponse);

    }
}
