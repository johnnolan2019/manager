package com.cit.micro.manager.client;

import com.cit.micro.logger.LoggerGrpc;
import com.cit.micro.logger.RemoteLog;
import com.cit.micro.logger.ReturnBool;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GrpcLoggerClient {

    private String serviceName = "Manager_Service";
    private String host = "localhost";
    private int port = 6565;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void info(String message) {
        String localDate = LocalDateTime.now().format(formatter);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        LoggerGrpc.LoggerBlockingStub stub = LoggerGrpc.newBlockingStub(channel);

        ReturnBool logResponse = stub.info(RemoteLog.newBuilder()
                .setTimeStamp(localDate)
                .setServiceName(serviceName)
                .setMessage(message)
                .build());

        channel.shutdown();
    }

    public void error(String message) {
        String localDate = LocalDateTime.now().format(formatter);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        LoggerGrpc.LoggerBlockingStub stub = LoggerGrpc.newBlockingStub(channel);

        ReturnBool logResponse = stub.error(RemoteLog.newBuilder()
                .setTimeStamp(localDate)
                .setServiceName(serviceName)
                .setMessage(message)
                .build());

        channel.shutdown();
    }

    public void debug(String message){
        String localDate = LocalDateTime.now().format(formatter);
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        LoggerGrpc.LoggerBlockingStub stub = LoggerGrpc.newBlockingStub(channel);

        ReturnBool logResponse = stub.debug(RemoteLog.newBuilder()
                .setTimeStamp(localDate)
                .setServiceName(serviceName)
                .setMessage(message)
                .build());

        channel.shutdown();
    }
}
