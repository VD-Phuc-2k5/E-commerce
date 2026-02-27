package com.dontwait.server.util;

import java.security.SecureRandom;

public class GenerateOTP{
    public static String generateSecretKey() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[16];
        secureRandom.nextBytes(bytes);
        StringBuilder secretKey = new StringBuilder();
        for (byte b : bytes) {
            secretKey.append(String.format("%02x", b));
        }
        return secretKey.toString();
    }
    public static String generateOTP(int length) {
        String allowedChars = "0123456789";
        StringBuilder otp = new StringBuilder();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            otp.append(allowedChars.charAt(index));
        }
        return otp.toString();
    }
}