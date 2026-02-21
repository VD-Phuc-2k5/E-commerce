package com.dontwait.server.dto.response.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VerifyOTPResponse {
    boolean isNewUser;

    // Case A: new user → cần đăng ký
    String registerToken;

    // Case B: existing user → đăng nhập luôn
    String accessToken;
    String refreshToken;
    String kycStatus;
}
