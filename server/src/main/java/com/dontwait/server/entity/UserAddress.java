package com.dontwait.server.entity;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddress {
    Long addressId;
    UUID userId;
    String address;
    String type;
    Boolean isDefault;
    String positionMap;
    String phone;
}
