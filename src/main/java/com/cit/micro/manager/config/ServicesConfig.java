package com.cit.micro.manager.config;

import org.springframework.beans.factory.annotation.Value;

public class ServicesConfig {
    @Value("${mqtt.broker.host}")
    public static final String MQTT_BROKER = "tcp://13.82.192.85:8883";

    @Value("${mqtt.topic.name}")
    public static final String MQTT_TOPIC = "validation.alerts.bravo";
}
