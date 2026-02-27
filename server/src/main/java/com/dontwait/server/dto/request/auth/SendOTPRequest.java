package com.dontwait.server.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class SendOTPRequest {
    @NotBlank(message = "PHONE_NUMBER_REQUIRED")
    @Pattern(regexp = "^\\+?\\d{10,15}$", message = "INVALID_PHONE_NUMBER")
    String phone;
    @Builder.Default
    @NotBlank(message = "COUNTRY_CODE_REQUIRED")
    @Pattern(regexp = "^\\+?\\d{1,4}$", message = "INVALID_COUNTRY_CODE")
    String countryCode = "+84";
}