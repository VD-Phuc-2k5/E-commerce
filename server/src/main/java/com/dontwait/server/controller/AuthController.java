package com.dontwait.server.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dontwait.server.dto.request.auth.LoginRequest;
import com.dontwait.server.dto.request.auth.RegisterRequest;
import com.dontwait.server.dto.request.auth.SendOTPRequest;
import com.dontwait.server.dto.request.auth.VerifyOTPRequest;
import com.dontwait.server.dto.response.ApiResponse;
import com.dontwait.server.dto.response.auth.LoginResponse;
import com.dontwait.server.dto.response.auth.RegisterResponse;
import com.dontwait.server.dto.response.auth.SendOTPResponse;
import com.dontwait.server.dto.response.auth.VerifyOTPResponse;
import com.dontwait.server.service.AuthService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/send-otp")
    public ApiResponse<SendOTPResponse> sendOtp(@Valid @RequestBody SendOTPRequest request) {
        SendOTPResponse response = authService.sendOtp(request);
        return ApiResponse.<SendOTPResponse>builder()
                .message("OTP sent successfully")
                .result(response)
                .build();
    }

    @PostMapping("/verify-otp")
    public ApiResponse<VerifyOTPResponse> verifyOtp(@Valid @RequestBody VerifyOTPRequest request) {
        VerifyOTPResponse response = authService.verifyOtp(request);
        String message = response.getIsNewUser()
                ? "OTP verified. Please register to continue."
                : "OTP verified. Login successful.";
        return ApiResponse.<VerifyOTPResponse>builder()
                .message(message)
                .result(response)
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request,
            @RequestParam(defaultValue = "buyer") String type) {
        RegisterResponse response = authService.register(request, type);
        return ApiResponse.<RegisterResponse>builder()
                .message("Registration successful")
                .result(response)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .message("Login successful")
                .result(response)
                .build();
    }
}
