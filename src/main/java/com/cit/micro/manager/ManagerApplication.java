package com.cit.micro.manager;

import com.cit.micro.manager.client.GrpcLoggerClient;
import com.cit.micro.manager.service.GrpcServerListener;
import com.cit.micro.manager.service.ManagerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ManagerApplication {
    final static GrpcLoggerClient logger = new GrpcLoggerClient();

    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
        ManagerService.subscribeToChannels();
        GrpcServerListener.serverRun();
    }

}
