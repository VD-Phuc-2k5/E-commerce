package com.dontwait.server.service;

import java.util.UUID;

import com.dontwait.server.dto.request.seller.RegisterSellerInfoRequest;

public interface SellerService {
    Boolean updateInfoSeller(RegisterSellerInfoRequest request, UUID userId);
    
}
