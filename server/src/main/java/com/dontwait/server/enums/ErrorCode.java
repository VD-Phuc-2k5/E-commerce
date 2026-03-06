package com.dontwait.server.enums;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    INVALID_ID_KEY(1001, "Invalid Message key, you should check your key", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED(6789, "Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission to access this resource", HttpStatus.FORBIDDEN),
    INVALID_PHONE_NUMBER(1007, "Invalid phone number format", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_REQUIRED(1008, "Phone number is required", HttpStatus.BAD_REQUEST),
    COUNTRY_CODE_REQUIRED(1009, "Country code is required", HttpStatus.BAD_REQUEST),
    INVALID_COUNTRY_CODE(1010, "Invalid country code format", HttpStatus.BAD_REQUEST), 
    OTP_VERIFY_LIMIT_EXCEEDED(1011, "OTP verification limit exceeded", HttpStatus.TOO_MANY_REQUESTS), 
    OTP_EXPIRED(1012, "OTP has expired", HttpStatus.BAD_REQUEST),
    OTP_RATE_LIMIT_EXCEEDED(1013, "OTP rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS),
    SMS_SEND_FAILED(1014, "Failed to send SMS", HttpStatus.INTERNAL_SERVER_ERROR),
    OTP_INVALID(1015, "Invalid OTP code", HttpStatus.BAD_REQUEST),
    OTP_REQUIRED(1016, "OTP code is required", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1017, "User not found", HttpStatus.NOT_FOUND),
    WRONG_PASSWORD(1018, "Wrong password", HttpStatus.UNAUTHORIZED),
    PASSWORD_REQUIRED(1019, "Password is required", HttpStatus.BAD_REQUEST),
    PASSWORD_LENGTH_INVALID(1020, "Password must be between 8 and 50 characters", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_REQUIRED(1021, "Confirm password is required", HttpStatus.BAD_REQUEST),
    PASSWORDS_NOT_MATCH(1022, "Password and confirm password do not match", HttpStatus.BAD_REQUEST),
    REGISTER_TOKEN_REQUIRED(1023, "Register token is required", HttpStatus.BAD_REQUEST),
    REGISTER_TOKEN_INVALID(1024, "Register token is invalid or expired", HttpStatus.BAD_REQUEST),
    PHONE_ALREADY_REGISTERED(1025, "Phone number is already registered", HttpStatus.CONFLICT),
    IDENTIFIER_REQUIRED(1026, "Identifier (phone/email/username) is required", HttpStatus.BAD_REQUEST),
    USER_ALREADY_EXISTS(1027, "User already exists with verified phone", HttpStatus.CONFLICT),
    TYPE_INVALID(1028, "Your type client send not matchs in server", HttpStatus.CONFLICT),
    SHOP_NAME_INVALID(1029, "Shop name must be between 3 and 50 characters", HttpStatus.BAD_REQUEST),
    SHOP_EMAIL_INVALID(1030, "Shop email is invalid", HttpStatus.BAD_REQUEST),
    SHOP_PHONE_INVALID(1031, "Shop phone number is invalid", HttpStatus.BAD_REQUEST),
    SHOP_DESCRIPTION_INVALID(1032, "Shop description must not exceed 500 characters", HttpStatus.BAD_REQUEST),
    PICKUP_ADDRESS_INVALID(1033, "Pickup address must not exceed 255 characters", HttpStatus.BAD_REQUEST),
    USER_INFO_CONFLICT(1034, "User email or phone do not match after registered", HttpStatus.CONFLICT),
    POSSTION_MAP_INVALID(1035, "Postion map must not exceed 255 characters", HttpStatus.BAD_REQUEST),
    USER_INFO_ALREADY_EXISTS(1036, "User already has seller info", HttpStatus.CONFLICT),
    SHOP_EMAIL_NOT_BLANK(1037, "Shop email must not be blank", HttpStatus.BAD_REQUEST),
    SHOP_PHONE_NOT_BLANK(1038, "Shop phone must not be blank", HttpStatus.BAD_REQUEST);
    int code;
    String message;
    HttpStatus httpStatus; 
}
