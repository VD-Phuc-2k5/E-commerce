package com.dontwait.server.service;

import com.dontwait.server.dto.request.seller.SellerUpdateRequest;

public interface SellerService {
    Boolean updateInfoSeller(SellerUpdateRequest request, String userId);
    
}
