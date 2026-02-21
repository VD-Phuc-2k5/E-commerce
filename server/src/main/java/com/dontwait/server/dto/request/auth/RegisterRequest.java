package com.dontwait.server.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class RegisterRequest {
    @NotBlank(message = "PHONE_NUMBER_REQUIRED")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "INVALID_PHONE_NUMBER")
    String phone;

    @NotBlank(message = "REGISTER_TOKEN_REQUIRED")
    String registerToken;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 50, message = "PASSWORD_LENGTH_INVALID")
    String password;

    @NotBlank(message = "CONFIRM_PASSWORD_REQUIRED")
    String confirmPassword;
}
