package com.dontwait.server.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import lombok.experimental.FieldDefaults;

@Configuration
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class TwilioInitializer {
    TwilioConfig twilioConfig;
    static Logger LOGGER = LoggerFactory.getLogger(TwilioInitializer.class);

    @Autowired
    public TwilioInitializer(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
        if (twilioConfig.getAccountSid().isBlank() || twilioConfig.getAuthToken().isBlank()) {
            LOGGER.warn("Twilio credentials missing, SMS features disabled");
            return;
        }

        Twilio.init(twilioConfig.getAccountSid(),
                twilioConfig.getAuthToken());
        LOGGER.info("Twilio initialized with account SID: {}",
                twilioConfig.getAccountSid());
    }

}
