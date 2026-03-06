package com.dontwait.server.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dontwait.server.dto.request.seller.RegisterSellerInfoRequest;
import com.dontwait.server.entity.UserAddress;
import com.dontwait.server.entity.UserInfo;
import com.dontwait.server.enums.ErrorCode;
import com.dontwait.server.exception.AppException;
import com.dontwait.server.mapper.UserAddressMapper;
import com.dontwait.server.mapper.UserInfoMapper;
import com.dontwait.server.mapper.UserMapper;
import com.dontwait.server.service.SellerService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerServiceImpl implements SellerService {

    UserMapper userMapper;
    UserInfoMapper userInfoMapper;
    UserAddressMapper userAddressMapper;

    @Override
    public Boolean updateInfoSeller(RegisterSellerInfoRequest request, UUID userId) {
        // 1. Kiểm tra user tồn tại
        if (userMapper.findById(userId) == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        // 2. Kiểm tra UserInfo đã tồn tại chưa (tránh insert trùng)
        if (userInfoMapper.findByUserId(userId) != null) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }

        // 3. Insert UserInfo — FE đã tự fill dữ liệu trước khi gửi lên
        UserInfo userInfo = UserInfo.builder()
                .userId(userId)
                .shopName(request.getShopName())
                .shopEmail(request.getShopEmail())
                .shopDescription(request.getShopDescription())
                .phone(request.getShopPhone())
                .pickupAddress(request.getPickupAddress())
                .build();
        userInfoMapper.insertUserInfo(userInfo);

        // 4. Insert UserAddress nếu có pickupAddress
        if (request.getPickupAddress() != null && !request.getPickupAddress().isBlank()) {
            UserAddress newAddress = UserAddress.builder()
                    .userId(userId)
                    .address(request.getPickupAddress())
                    .type("PICKUP")
                    .isDefault(true)
                    .phone(request.getShopPhone())
                    .build();
            userAddressMapper.insertUserAddress(newAddress);
        }

        return true;
    }
}
