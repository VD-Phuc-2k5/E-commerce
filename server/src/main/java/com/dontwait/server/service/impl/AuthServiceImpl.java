package com.dontwait.server.service.impl;

import java.time.Instant;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dontwait.server.dto.request.auth.LoginRequest;
import com.dontwait.server.dto.request.auth.RegisterRequest;
import com.dontwait.server.dto.request.auth.SendOTPRequest;
import com.dontwait.server.dto.request.auth.VerifyOTPRequest;
import com.dontwait.server.dto.response.auth.LoginResponse;
import com.dontwait.server.dto.response.auth.RegisterResponse;
import com.dontwait.server.dto.response.auth.SendOTPResponse;
import com.dontwait.server.dto.response.auth.VerifyOTPResponse;
import com.dontwait.server.entity.User;
import com.dontwait.server.enums.ErrorCode;
import com.dontwait.server.exception.AppException;
import com.dontwait.server.mapper.UserMapper;
import com.dontwait.server.service.AuthService;
import com.dontwait.server.service.JwtService;
import com.dontwait.server.service.OTPService;
// import com.dontwait.server.service.SmsSenderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    OTPService otpService;
    // SmsSenderService smsSenderService;
    JwtService jwtService;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    private void validateType(String type) {
        if (!type.equals("SELLER") && !type.equals("BUYER")) {
            throw new AppException(ErrorCode.TYPE_INVALID);
        }
    }

    // ==================== SEND OTP ====================
    @Override
    public SendOTPResponse sendOtp(SendOTPRequest request) {
        String phone = request.getPhone();

        if (phone.startsWith("0")) {
            phone = request.getCountryCode() + phone.substring(1);
        }

        String otp = otpService.generateAndSaveOtp(phone);
        log.info("======== [DEV] OTP for {}: {} ========", phone, otp);

        Instant sentAt = Instant.now();
        long ttlSeconds = otpService.getOtpTtlSeconds();
        long expiresInMs = ttlSeconds * 1000;
        Instant expiresAt = sentAt.plusSeconds(ttlSeconds);

        return SendOTPResponse.builder()
                .otpSentAt(sentAt.toString())
                .expiresIn(expiresInMs)
                .expiresAt(expiresAt.toString())
                .message("OTP has been sent to " + phone)
                .build();
    }

    // ==================== VERIFY OTP ====================
    @Override
    public VerifyOTPResponse verifyOtp(VerifyOTPRequest request, String type) {
        String normalizedType = type.toUpperCase();
        validateType(normalizedType);

        String phone = request.getPhone();
        String otpCode = request.getOtpCode();

        boolean isValid = otpService.validateOtp(phone, otpCode);
        if (!isValid) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        User existingUser = userMapper.findByPhone(phone);

        if (existingUser != null && existingUser.isPhoneVerified()) {
            // Case B: User cũ → đăng nhập luôn
            Set<String> roles = userMapper.findRolesByUserId(existingUser.getUserId());
            existingUser.setRoles(roles);

            String typeFound = roles.stream()
                    .filter(r -> r.equalsIgnoreCase(normalizedType))
                    .findFirst()
                    .orElseThrow(() -> new AppException(ErrorCode.TYPE_INVALID));

            String accessToken = jwtService.generateAccessToken(existingUser);
            String refreshToken = jwtService.generateRefreshToken(existingUser);

            return VerifyOTPResponse.builder()
                    .isNewUser(false)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .type(typeFound)
                    .kycStatus(existingUser.getKycStatus())
                    .build();
        } else {
            // Case A: User mới → trả registerToken để đăng ký
            String registerToken = jwtService.generateRegisterToken(phone);

            if (existingUser != null) {
                userMapper.setPhoneVerified(phone);
            }

            return VerifyOTPResponse.builder()
                    .isNewUser(true)
                    .registerToken(registerToken)
                    .build();
        }
    }

    // ==================== REGISTER ====================
    @Override
    public RegisterResponse register(RegisterRequest request, String type) {
        String normalizedType = type.toUpperCase();
        validateType(normalizedType);

        String phone = jwtService.extractPhoneFromRegisterToken(request.getRegisterToken());
        if (phone == null) {
            throw new AppException(ErrorCode.REGISTER_TOKEN_INVALID);
        }

        if (!phone.equals(request.getPhone())) {
            throw new AppException(ErrorCode.REGISTER_TOKEN_INVALID);
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORDS_NOT_MATCH);
        }

        User existingUser = userMapper.findByPhone(phone);
        if (existingUser != null && existingUser.isPhoneVerified() && existingUser.getUserPassword() != null) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = User.builder()
                .userPhone(phone)
                .userPassword(hashedPassword)
                .name(phone)
                .phoneVerified(true)
                .build();
        userMapper.insertUser(newUser);

        User createdUser = userMapper.findByPhone(phone);

        String role = switch (normalizedType) {
            case "SELLER" -> "SELLER";
            case "BUYER" -> "BUYER";
            default -> throw new AppException(ErrorCode.TYPE_INVALID);
        };
        userMapper.insertUserRole(createdUser.getUserId(), role);

        Set<String> roles = new HashSet<>();
        roles.add(role);
        createdUser.setRoles(roles);

        String accessToken = jwtService.generateAccessToken(createdUser);
        String refreshToken = jwtService.generateRefreshToken(createdUser);

        return RegisterResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // ==================== LOGIN (by password) ====================
    @Override
    public LoginResponse login(LoginRequest request, String type) {
        String normalizedType = type.toUpperCase();
        validateType(normalizedType);

        String identifier = request.getIdentifier();
        User user = userMapper.findByPhone(identifier);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getUserPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        if (user.getUserId() == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        String userId = user.getUserId().toString();

        Set<String> roles = userMapper.findRolesByUserId(user.getUserId());
        user.setRoles(roles);

        String typeFound = roles.stream()
                .filter(r -> r.equalsIgnoreCase(normalizedType))
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.TYPE_INVALID));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .name(user.getName())
                .type(typeFound)
                .avatar(user.getUserAvatar())
                .build();
    }
}
