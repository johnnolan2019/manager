package com.cit.micro.manager.service.subscriber;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;


public class MqttSubscribe implements IMqttSubscribe{
    /**
     * Member Vars
     */
    private String topic;
    private static final String ENCODING = "UTF-8";
    private String name;
    private String clientId = null;
    private MqttAsyncClient client;
    private String mqttBroker;
    private MemoryPersistence memoryPersistence;
    private IMqttToken connectToken;
    private IMqttToken subscribeToken;
    private String userContext = "default";
    private static final int qos = 2;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ApplicationEventPublisher applicationEventPublisher;

    /**
     * Getters/setters
     */

    public void setClientId(String clientId) { this.clientId = clientId; }

    /**
     * Constructors
     */
    public MqttSubscribe(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
    public MqttSubscribe(String name) { this.name = name; }

    /**
     * Initialize a connection with the given MQTT Broker
     */
    @Override
    public void connect(String broker, String mqttTopic)
    {
        try {
            this.topic = mqttTopic;
            this.mqttBroker = broker;
            MqttConnectOptions options = new MqttConnectOptions();
            memoryPersistence = new MemoryPersistence();
            if (this.clientId == null){
                this.clientId = GenerteId.generateClientId();
                if (log.isDebugEnabled()){
                    log.debug("Had to set clientID using generateClient");
                }
            }
            if (log.isInfoEnabled()) {
                log.info(this.clientId);
            }
            options.setKeepAliveInterval(100);
            options.setCleanSession(true);
            client = new MqttAsyncClient(broker, this.clientId, memoryPersistence);
            client.setCallback(this);
            connectToken = client.connect(options, null, this);
        } catch (MqttException e) {
            log.error("Threw an Exception in MqttSubscribe::connect, full stack trace follows:", e);
        }
    }

    @Override
    public boolean isConnected() {
        return (client != null) && (client.isConnected());
    }

    @Override
    public void connectionLost(Throwable cause) {
        // The MQTT client lost the connection, need to add reconnect...
        log.error("Threw an Exception in MqttSubscribe::connectionLost, full stack trace follows:",cause);
        clientId = null;
        connect(mqttBroker,topic);

    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        if (asyncActionToken.equals(connectToken)) {
            if (log.isInfoEnabled()) {
                log.info("Connection made");
            }
            try {
                subscribeToken = client.subscribe(topic, qos, userContext, this);
                subscribeToken.waitForCompletion(10000);
            } catch (MqttException e) {
                log.error("Threw an Exception in MqttSubscribe::onSuccess, full stack trace follows:",e);
            }
        }
        else if (asyncActionToken.equals(subscribeToken))
        {
            log.info("{} subscribed to the {} topic", name, topic);
        }
    }


    /**
     * Terminate the MQTT client connection.
     */
    @Override
    public void terminate()
    {
        try {
            this.client.disconnect();
        }
        catch (MqttException e) {
            log.error("Threw an MqttException in MqttSubscribe::terminate, full stack trace follows:",e);
        }
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception)
    {
        log.error("Threw an Exception in MqttSubscribe::onFailure, full stack trace follows:",exception);
    }

    /**
     * MessageArrived handle broker communicaions
     *
     * @param topic the topic for the MQTT message
     * @param message the message that will be sent
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception
    {
        if (!topic.equals(this.topic)) {
            return;
        }
        String messageText = new String(message.getPayload(), ENCODING);
        if (log.isInfoEnabled()) {
            log.info("{} received {}: {}", name, topic, messageText);
        }
        String[] keyValue = messageText.split(":");

        // todo implement this properly..
        applicationEventPublisher.publishEvent("");
    }

    /**
     * Called if delivery is verified
     *
     * @param token deliverytoken, verification from broker
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        if (log.isInfoEnabled()) {
            log.info("delivery complete");
        }
    }
}
