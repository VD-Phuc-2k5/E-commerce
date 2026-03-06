package com.dontwait.server.dto.response.user.seller;
import com.dontwait.server.dto.response.user.UserResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerResponse extends UserResponse {
    String shopName;
    String shopPhone;
    String shopEmail;
    String pickupAddress;
}
