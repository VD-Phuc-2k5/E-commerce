package com.dontwait.server.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.dontwait.server.enums.ErrorCode;
import com.dontwait.server.exception.AppException;
import com.dontwait.server.service.OTPService;
import com.dontwait.server.util.GenerateOTP;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class OTPServiceImpl implements OTPService {

    StringRedisTemplate redisTemplate;

    static final String OTP_PREFIX = "OTP:";
    static final String SEND_RATE_PREFIX = "OTP_SEND_RATE:"; // Giới hạn số lần GỬI
    static final String VERIFY_RATE_PREFIX = "OTP_VERIFY_RATE:"; // Giới hạn số lần XÁC THỰC
    public static final long OTP_TTL_MINUTES = 5;
    static final int MAX_SEND_REQUESTS = 3; // Tối đa 3 lần gửi / 5 phút
    static final int MAX_VERIFY_ATTEMPTS = 5; // Tối đa 5 lần nhập sai / 5 phút

    @Override
    public String generateAndSaveOtp(String phoneNumber) {
        // Rate limit: giới hạn số lần GỬI OTP
        checkRateLimit(SEND_RATE_PREFIX + phoneNumber,
                MAX_SEND_REQUESTS,
                ErrorCode.OTP_RATE_LIMIT_EXCEEDED);

        // Tạo OTP 6 số
        String otp = GenerateOTP.generateOTP(6);

        // Lưu OTP vào Redis, TTL 5 phút
        redisTemplate.opsForValue().set(OTP_PREFIX + phoneNumber,
                otp,
                OTP_TTL_MINUTES,
                TimeUnit.MINUTES);

        // Tăng counter gửi
        incrementRateLimit(SEND_RATE_PREFIX + phoneNumber);

        return otp;
    }

    @Override
    public boolean validateOtp(String phoneNumber, String otp) {
        // Rate limit: giới hạn số lần NHẬP SAI
        checkRateLimit(VERIFY_RATE_PREFIX + phoneNumber, 
                MAX_VERIFY_ATTEMPTS, 
                ErrorCode.OTP_VERIFY_LIMIT_EXCEEDED);

        String savedOtp = redisTemplate.opsForValue().get(OTP_PREFIX + phoneNumber);

        if (savedOtp == null) {
            throw new AppException(ErrorCode.OTP_EXPIRED);
        }

        if (!savedOtp.equals(otp)) {
            // Nhập sai → tăng counter verify
            incrementRateLimit(VERIFY_RATE_PREFIX + phoneNumber);
            return false;
        }

        // Nhập đúng → xóa OTP + reset verify counter
        deleteOtp(phoneNumber);
        redisTemplate.delete(VERIFY_RATE_PREFIX + phoneNumber);
        return true;
    }

    @Override
    public void deleteOtp(String phoneNumber) {
        redisTemplate.delete(OTP_PREFIX + phoneNumber);
    }

    @Override
    public long getOtpTtlSeconds() {
        return OTP_TTL_MINUTES * 60;
    }

    private void checkRateLimit(String key, int maxRequests, ErrorCode errorCode) {
        String count = redisTemplate.opsForValue().get(key);
        if (count != null && Integer.parseInt(count) >= maxRequests) {
            throw new AppException(errorCode);
        }
    }

    private void incrementRateLimit(String key) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            redisTemplate.expire(key, OTP_TTL_MINUTES, TimeUnit.MINUTES);
        }
    }
}
