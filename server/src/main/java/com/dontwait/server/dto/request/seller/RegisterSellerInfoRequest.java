package com.dontwait.server.dto.request.seller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class RegisterSellerInfoRequest {

    @Size(min = 3, max = 50, message = "SHOP_NAME_INVALID")
    String shopName; // default = username

    @Size(max = 255, message = "PICKUP_ADDRESS_INVALID")
    String pickupAddress; // default = shippingAddress

    @Size(max = 255, message = "POSSTION_MAP_INVALID")
    String positionMap; // default = shippingAddress possitionMap

    @Email(message = "SHOP_EMAIL_INVALID")
    @Size(max = 100, message = "SHOP_EMAIL_INVALID")
    String shopEmail; // default = buyer email

    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "SHOP_PHONE_INVALID")
    String shopPhone; // default = phone verified

    @Size(max = 500, message = "SHOP_DESCRIPTION_INVALID")
    String shopDescription;
}
