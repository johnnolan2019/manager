package com.cit.micro.manager.client;

import com.cit.micro.logger.LoggerGrpc;
import com.cit.micro.logger.RemoteLog;
import com.cit.micro.logger.ReturnBool;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.time.LocalDate;

public class GrpcLoggerClient {

    public void info(String message) {
        LocalDate localDate = LocalDate.now();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        LoggerGrpc.LoggerBlockingStub stub = LoggerGrpc.newBlockingStub(channel);

        ReturnBool logResponse = stub.info(RemoteLog.newBuilder()
                .setTimeStamp(localDate.toString())
                .setServiceName("Manager_Service")
                .setMessage(message)
                .build());

        channel.shutdown();
    }

    public void error(String message) {
        LocalDate localDate = LocalDate.now();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        LoggerGrpc.LoggerBlockingStub stub = LoggerGrpc.newBlockingStub(channel);

        ReturnBool logResponse = stub.error(RemoteLog.newBuilder()
                .setTimeStamp(localDate.toString())
                .setServiceName("Manager_Service")
                .setMessage(message)
                .build());

        channel.shutdown();
    }

    public void debug(String message){
        LocalDate localDate = LocalDate.now();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        LoggerGrpc.LoggerBlockingStub stub = LoggerGrpc.newBlockingStub(channel);

        ReturnBool logResponse = stub.debug(RemoteLog.newBuilder()
                .setTimeStamp(localDate.toString())
                .setServiceName("Manager_Service")
                .setMessage(message)
                .build());

        channel.shutdown();
    }
}
