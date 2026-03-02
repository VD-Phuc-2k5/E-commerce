package com.dontwait.server.dto.request.seller;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellerUpdateRequest {
    String shopName; //default = username
    String pickupAddress; //default = shippingAddress
    String email; // default buyer email
    String phone; //default phone = phone verified
}
