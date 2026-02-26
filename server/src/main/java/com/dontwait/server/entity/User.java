package com.dontwait.server.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

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
public class User {
    UUID userId;
    String username;
    String name;
    String userEmail;
    String userPhone;
    String userPassword;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String userAvatar;
    String userGender;
    String userStatus;
    LocalDate userDob;
    String userAddress;
    boolean phoneVerified;
    boolean emailVerified;
    String kycStatus;
    String shopName;
    String idCardNumber;
    String idCardFrontUrl;
    String idCardBackUrl;
    Set<String> roles;
}
