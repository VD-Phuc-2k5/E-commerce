package com.dontwait.server.dto.response.user;
import java.time.LocalDate;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    UUID userId;
    String username;
    String userEmail;
    String userPhone;
    String userAvatar;
    String userGender;
    LocalDate userDob;
    String userAddress;
}
