package com.cit.micro.manager.service.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerteId {
    private static final Logger log = LoggerFactory.getLogger(GenerteId.class);

    public static String generateClientId(){
        String generatedString = generateSafeToken();
        log.info("new client ID is: {}",generatedString);
        return generatedString;
    }

    private static String generateSafeToken() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[15];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return token;
    }
}
