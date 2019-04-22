package com.cit.micro.manager;

import com.cit.micro.manager.client.GrpcLoggerClient;
import com.cit.micro.manager.service.GrpcManager;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class ManagerApplication {

    public static void main(String[] args) {
        final GrpcLoggerClient logger = new GrpcLoggerClient();

        SpringApplication.run(ManagerApplication.class, args);
        Server server = ServerBuilder
                .forPort(6569)
                .addService(new GrpcManager()).build();
        logger.info("Manager service now running ");
        try{
            server.start();
            server.awaitTermination();
        }catch (
                IOException e){
            logger.error("bad");
        }catch (InterruptedException e){
            logger.error("Not as bad, but not good");
        }
    }

}
