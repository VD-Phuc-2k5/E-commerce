package com.dontwait.server.service.impl;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dontwait.server.dto.request.seller.RegisterSellerInfoRequest;
import com.dontwait.server.entity.User;
import com.dontwait.server.enums.ErrorCode;
import com.dontwait.server.exception.AppException;
import com.dontwait.server.mapper.UserMapper;
import com.dontwait.server.service.SellerService;

@Service
public class SellerServiceImpl implements SellerService {
    UserMapper userMapper;
    @Override
    public Boolean updateInfoSeller(RegisterSellerInfoRequest request, UUID userId) {
        User existingUser = userMapper.findById(userId);
        if (existingUser == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        return null;
    }
}
