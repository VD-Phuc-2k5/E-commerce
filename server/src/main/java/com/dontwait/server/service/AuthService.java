package com.dontwait.server.service;

import com.dontwait.server.dto.request.auth.VerifyOTPRequest;
import com.dontwait.server.dto.response.auth.VerifyOTPResponse;

public interface AuthService {
    VerifyOTPResponse verifyOtp(VerifyOTPRequest request);
}
