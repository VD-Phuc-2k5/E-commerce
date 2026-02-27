package com.dontwait.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dontwait.server.configuration.TwilioConfig;
import com.dontwait.server.enums.ErrorCode;
import com.dontwait.server.exception.AppException;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@ExtendWith(MockitoExtension.class)
class TwilioProviderSmsSenderTest {

    @Mock
    TwilioConfig twilioConfig;

    @InjectMocks
    TwilioProviderSmsSender smsSender;

    static final String PHONE = "+84901234567";
    static final String FROM_PHONE = "+15005550006";
    static final String SMS_MESSAGE = "Your OTP code is: 123456. Valid for 5 minutes.";

    @Test
    @DisplayName("Gửi SMS thành công qua Twilio")
    void shouldSendSms_Successfully() {
        // Given
        when(twilioConfig.getFromPhoneNumber()).thenReturn(FROM_PHONE);

        Message mockMessage = mock(Message.class);
        when(mockMessage.getSid()).thenReturn("SM1234567890");
        when(mockMessage.getStatus()).thenReturn(Message.Status.QUEUED);

        MessageCreator mockCreator = mock(MessageCreator.class);
        when(mockCreator.create()).thenReturn(mockMessage);

        try (MockedStatic<Message> messageMock = mockStatic(Message.class)) {
            messageMock.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    eq(SMS_MESSAGE)
            )).thenReturn(mockCreator);

            // When & Then: không throw exception
            assertDoesNotThrow(() -> smsSender.sendSms(PHONE, SMS_MESSAGE));
        }
    }

    @Test
    @DisplayName("Throw SMS_SEND_FAILED khi Twilio API lỗi")
    void shouldThrowException_WhenTwilioApiFails() {
        // Given
        when(twilioConfig.getFromPhoneNumber()).thenReturn(FROM_PHONE);

        MessageCreator mockCreator = mock(MessageCreator.class);
        when(mockCreator.create()).thenThrow(new ApiException("Invalid phone number"));

        try (MockedStatic<Message> messageMock = mockStatic(Message.class)) {
            messageMock.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    eq(SMS_MESSAGE)
            )).thenReturn(mockCreator);

            // When & Then
            AppException exception = assertThrows(AppException.class,
                    () -> smsSender.sendSms(PHONE, SMS_MESSAGE));

            assertEquals(ErrorCode.SMS_SEND_FAILED, exception.getErrorCode());
        }
    }

    @Test
    @DisplayName("Throw SMS_SEND_FAILED khi lỗi unexpected")
    void shouldThrowException_WhenUnexpectedError() {
        // Given
        when(twilioConfig.getFromPhoneNumber()).thenReturn(FROM_PHONE);

        MessageCreator mockCreator = mock(MessageCreator.class);
        when(mockCreator.create()).thenThrow(new RuntimeException("Network error"));

        try (MockedStatic<Message> messageMock = mockStatic(Message.class)) {
            messageMock.when(() -> Message.creator(
                    any(PhoneNumber.class),
                    any(PhoneNumber.class),
                    eq(SMS_MESSAGE)
            )).thenReturn(mockCreator);

            // When & Then
            AppException exception = assertThrows(AppException.class,
                    () -> smsSender.sendSms(PHONE, SMS_MESSAGE));

            assertEquals(ErrorCode.SMS_SEND_FAILED, exception.getErrorCode());
        }
    }
}
