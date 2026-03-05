package com.dontwait.server.service;

import java.util.UUID;

import com.dontwait.server.dto.request.seller.SellerUpdateRequest;

public interface SellerService {
    Boolean updateInfoSeller(SellerUpdateRequest request, UUID userId);
    
}
