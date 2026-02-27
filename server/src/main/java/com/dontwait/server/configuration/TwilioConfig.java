package com.dontwait.server.configuration;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
public class TwilioConfig{
    String accountSid;
    String authToken;
    String fromPhoneNumber;
}
