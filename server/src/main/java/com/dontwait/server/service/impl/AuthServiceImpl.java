package com.dontwait.server.service.impl;

import java.time.Instant;
import java.util.Set;
import java.util.HashSet;

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
    // SmsSenderService smsSenderService; // TODO: Uncomment khi Twilio hoạt động
    JwtService jwtService;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    // ==================== SEND OTP ====================
    @Override
    public SendOTPResponse sendOtp(SendOTPRequest request) {
        String phone = request.getPhone();

        // Generate OTP và lưu vào Redis
        String otp = otpService.generateAndSaveOtp(phone);

        // TODO: Uncomment khi Twilio hoạt động

        if(phone.startsWith("0")) {
            phone = request.getCountryCode() + phone.substring(1);
        }

        // smsSenderService.sendSms(phone, "Your OTP code is: " + otp + ". Valid for 5 minutes.");
        //
        // Log OTP ra console cho development (XÓA KHI LÊN PRODUCTION)
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
        String phone = request.getPhone();
        String otpCode = request.getOtpCode();

        // Validate OTP từ Redis (validateOtp đã tự xóa OTP khi đúng)
        boolean isValid = otpService.validateOtp(phone, otpCode);
        if (!isValid) {
            throw new AppException(ErrorCode.OTP_INVALID);
        }

        // Check user đã tồn tại chưa
        User existingUser = userMapper.findByPhone(phone);

        if (existingUser != null && existingUser.isPhoneVerified()) {
            // Case B: User cũ → đăng nhập luôn
            String accessToken = jwtService.generateAccessToken(existingUser);
            String refreshToken = jwtService.generateRefreshToken(existingUser);

            // Lấy role từ DB → trả FE dưới tên "type"
            Set<String> roles = userMapper.findRolesByUserId(existingUser.getUserId());
            existingUser.setRoles(roles);
            return VerifyOTPResponse.builder()
                    .isNewUser(false)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .type(type)
                    .kycStatus(existingUser.getKycStatus())
                    .build();
        } else {
            // Case A: User mới → trả registerToken để đăng ký
            String registerToken = jwtService.generateRegisterToken(phone);

            // Đánh dấu phone đã verified (nếu user chưa tồn tại thì sẽ tạo khi register)
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
        // 1. Verify registerToken
        String phone = jwtService.extractPhoneFromRegisterToken(request.getRegisterToken());
        if (phone == null) {
            throw new AppException(ErrorCode.REGISTER_TOKEN_INVALID);
        }

        // 2. Phone trong token phải khớp phone trong request
        if (!phone.equals(request.getPhone())) {
            throw new AppException(ErrorCode.REGISTER_TOKEN_INVALID);
        }

        // 3. Check password match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new AppException(ErrorCode.PASSWORDS_NOT_MATCH);
        }

        // 4. Check user đã tồn tại và đã verified chưa
        User existingUser = userMapper.findByPhone(phone);
        if (existingUser != null && existingUser.isPhoneVerified() && existingUser.getUserPassword() != null) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 5. Tạo user mới
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        User newUser = User.builder()
                .userPhone(phone)
                .userPassword(hashedPassword)
                .name(phone) // Dùng phone làm name tạm, user sẽ cập nhật sau
                .phoneVerified(true)
                .build();
        userMapper.insertUser(newUser);

        // 6. Lấy user vừa tạo để lấy userId (do DB generate UUID)
        User createdUser = userMapper.findByPhone(phone);
        String userId = createdUser.getUserId().toString();

        // 7. Gán role theo type từ query param (seller → SELLER, buyer → BUYER)
        String role = switch (type.toLowerCase()) {
            case "seller" -> "SELLER";
            case "buyer" -> "BUYER";
            default -> "BUYER";
        };
        userMapper.insertUserRole(createdUser.getUserId(), role);

        Set<String> roles = new HashSet<>();
        roles.add(role);
        createdUser.setRoles(roles);
        // 8. Generate tokens
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
        String identifier = request.getIdentifier();

        // Tìm user theo phone/email/username
        // Hiện tại chỉ hỗ trợ phone, sau sẽ mở rộng email + username
        User user = userMapper.findByPhone(identifier);

        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        // Check user đã set password chưa (có thể verify OTP nhưng chưa register)
        if (user.getUserPassword() == null || user.getUserPassword().isEmpty()) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getUserPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }
        String userId = user.getUserId().toString();
        if (userId == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        // Lấy role từ bảng user_roles → trả về FE dưới tên "type"
        Set<String> roles = userMapper.findRolesByUserId(user.getUserId());
        user.setRoles(roles);
        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(userId)
                .name(user.getName())
                .type(type)
                .avatar(user.getUserAvatar())
                .build();
    }
}
