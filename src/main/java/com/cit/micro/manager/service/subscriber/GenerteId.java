package com.cit.micro.manager.service.subscriber;

import com.cit.micro.manager.client.GrpcLoggerClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;
import java.util.Base64;

public class GenerteId {
    private final static GrpcLoggerClient log = new GrpcLoggerClient();

    public static String generateClientId(){
        String generatedString = generateSafeToken();
        log.info(String.format("new client ID is: %s", generatedString));
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
