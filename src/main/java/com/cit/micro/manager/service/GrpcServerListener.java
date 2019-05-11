package com.cit.micro.manager.service;

import com.cit.micro.manager.client.GrpcLoggerClient;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GrpcServerListener {
    private static final GrpcLoggerClient logger = new GrpcLoggerClient();
    private static GrpcManager grpcManager;
    private    static int port = 6569;

    @Autowired
    public GrpcServerListener(GrpcManager grpcManagerInjected){
        grpcManager = grpcManagerInjected;
    }

    public static void serverRun(){
        Server server = ServerBuilder
                .forPort(port)
                .addService(grpcManager).build();
        logger.info("Manager service now running ");
        try{
            server.start();
            server.awaitTermination();
        }catch (
                IOException e){
            logger.error("Manager Service threw IO exception");
        }catch (InterruptedException e){
            logger.error("Manager Service threw Interrupted exception");
        }
    }
}
