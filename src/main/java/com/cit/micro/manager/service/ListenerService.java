package com.cit.micro.manager.service;

import com.cit.micro.manager.client.GrpcDataClient;
import com.cit.micro.manager.client.GrpcLoggerClient;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerService implements ApplicationListener<AlertEvent> {
    private final GrpcLoggerClient log = new GrpcLoggerClient();
    private GrpcDataClient grpcDataClient;

    @Override
    public void onApplicationEvent(AlertEvent event) {
        grpcDataClient.add(event.getLogData());
        log.info("Adding data to DB after event");
    }

}
