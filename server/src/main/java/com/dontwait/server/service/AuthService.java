package com.dontwait.server.service;

import com.dontwait.server.dto.request.auth.LoginRequest;
import com.dontwait.server.dto.request.auth.RegisterRequest;
import com.dontwait.server.dto.request.auth.SendOTPRequest;
import com.dontwait.server.dto.request.auth.VerifyOTPRequest;
import com.dontwait.server.dto.response.auth.LoginResponse;
import com.dontwait.server.dto.response.auth.RegisterResponse;
import com.dontwait.server.dto.response.auth.SendOTPResponse;
import com.dontwait.server.dto.response.auth.VerifyOTPResponse;

public interface AuthService {
    SendOTPResponse sendOtp(SendOTPRequest request);
    VerifyOTPResponse verifyOtp(VerifyOTPRequest request, String type);
    RegisterResponse register(RegisterRequest request, String type);
    LoginResponse login(LoginRequest request, String type);
}
