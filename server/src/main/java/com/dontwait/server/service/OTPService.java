package com.dontwait.server.service;

public interface OTPService {
   String generateAndSaveOtp(String phoneNumber);
   boolean validateOtp(String phoneNumber, String otp);
   void deleteOtp(String phoneNumber);
   long getOtpTtlSeconds();
}
