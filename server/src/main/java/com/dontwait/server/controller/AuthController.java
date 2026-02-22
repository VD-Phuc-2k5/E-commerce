package com.dontwait.server.controller;

import java.time.Instant;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dontwait.server.dto.request.auth.SendOTPRequest;
import com.dontwait.server.dto.response.ApiResponse;
import com.dontwait.server.dto.response.auth.SendOTPResponse;
import com.dontwait.server.service.OTPService;
import com.dontwait.server.service.SmsSenderService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    SmsSenderService smsSenderService;
    OTPService otpService;

    @PostMapping("/send-otp")
    public ApiResponse<SendOTPResponse> sendOtp(@Valid @RequestBody SendOTPRequest request) {
        String otp = otpService.generateAndSaveOtp(request.getPhone());
        smsSenderService.sendSms(request.getPhone(), "Your OTP code is: " + otp + ". Valid for 5 minutes.");

        Instant sentAt = Instant.now();
        long ttlSeconds = otpService.getOtpTtlSeconds();
        long expiresInMs = ttlSeconds * 1000; // convert to milliseconds
        Instant expiresAt = sentAt.plusSeconds(ttlSeconds);

        return ApiResponse.<SendOTPResponse>builder()
                .message("OTP sent successfully")
                .result(SendOTPResponse.builder()
                        .otpSentAt(sentAt.toString())
                        .expiresIn(expiresInMs)
                        .expiresAt(expiresAt.toString())
                        .message("OTP has been sent to " + request.getPhone())
                        .build())
                .build();
    }
}
