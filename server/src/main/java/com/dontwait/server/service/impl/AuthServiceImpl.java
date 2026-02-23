package com.dontwait.server.service.impl;

import org.springframework.stereotype.Service;

import com.dontwait.server.dto.request.auth.VerifyOTPRequest;
import com.dontwait.server.dto.response.auth.VerifyOTPResponse;
import com.dontwait.server.service.AuthService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    @Override
    public VerifyOTPResponse verifyOtp(VerifyOTPRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyOtp'");
    }
    
}
