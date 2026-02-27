package com.dontwait.server.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.dontwait.server.configuration.TwilioConfig;
import com.dontwait.server.enums.ErrorCode;
import com.dontwait.server.exception.AppException;
import com.dontwait.server.service.SmsSenderService;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TwilioProviderSmsSender implements SmsSenderService {

    TwilioConfig twilioConfig;
    static final Logger logger = LoggerFactory.getLogger(TwilioProviderSmsSender.class);

    @Override
    public void sendSms(String phoneNumber, String message) {
        try {
            PhoneNumber to = new PhoneNumber(phoneNumber);
            logger.info("Sending SMS to: {}, From: {}, Message: {}",
                    phoneNumber, twilioConfig.getFromPhoneNumber(), message);
            PhoneNumber from = new PhoneNumber(twilioConfig.getFromPhoneNumber());

            Message twilioMessage = Message.creator(to, from, message).create();

            logger.info("SMS sent to: {}, SID: {}, Status: {}",
                    phoneNumber, twilioMessage.getSid(), twilioMessage.getStatus());
        } catch (ApiException e) {
            logger.error("Twilio API error sending to {}: [{}] {}",
                    phoneNumber, e.getCode(), e.getMessage());
            throw new AppException(ErrorCode.SMS_SEND_FAILED);
        } catch (Exception e) {
            logger.error("Unexpected error sending SMS to {}: {}", phoneNumber, e.getMessage());
            throw new AppException(ErrorCode.SMS_SEND_FAILED);
        }
    }
}
