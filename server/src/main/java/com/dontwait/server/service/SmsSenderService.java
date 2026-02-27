package com.dontwait.server.service;

public interface SmsSenderService {
    void sendSms(String phoneNumber, String message);
}
