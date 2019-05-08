package com.cit.micro.manager.service;

import com.cit.micro.manager.client.GrpcLoggerClient;
import com.cit.micro.manager.service.subscriber.GenerteId;
import com.cit.micro.manager.service.subscriber.MqttSubscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;


@Service
@PropertySource("classpath:application.properties")
public class SubscriberService {
    private final GrpcLoggerClient log = new GrpcLoggerClient();
    private MqttSubscribe subscriber;

    @Value("${manager.mqttBroker}")
    private String mqttBroker;

    @Autowired
    public SubscriberService(ApplicationEventPublisher applicationEventPublisher){
        this.subscriber = new MqttSubscribe(applicationEventPublisher);
    }

    public void subscribe(String mqttTopic){
        subscriber.setClientId(GenerteId.generateClientId());
        subscriber.connect(mqttBroker, mqttTopic);
    }

    public boolean connected(){
        return subscriber.isConnected();
    }
}
