package com.dontwait.server.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dontwait.server.dto.request.seller.RegisterSellerInfoRequest;
import com.dontwait.server.dto.response.ApiResponse;
import com.dontwait.server.service.SellerService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerController {
    SellerService sellerService;

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<Boolean>> RegisterInfoSeller(
            @Valid @RequestBody RegisterSellerInfoRequest request,
            @PathVariable UUID userId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.<Boolean>builder()
                        .message("Update info seller successfully")
                        .result(sellerService.updateInfoSeller(request, userId)).build());
    }

}
