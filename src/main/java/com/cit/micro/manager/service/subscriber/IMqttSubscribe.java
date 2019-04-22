package com.cit.micro.manager.service.subscriber;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.MqttCallback;

public interface IMqttSubscribe extends IMqttActionListener, MqttCallback {
    /**
     * Connect with the given MQTT Broker
     */
    void connect(String broker, String mqttTopic);

    /**
     * Check if there is a connection
     */
    boolean isConnected();


    /**
     * Terminate the connection from the MQTT Broker.
     */
    void terminate();

}
