package com.cit.micro.manager.client;

import com.cit.micro.data.AccessDBGrpc;
import com.cit.micro.data.Channel;
import com.cit.micro.data.Id;
import com.cit.micro.data.LogData;
import com.cit.micro.manager.Uid;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


@Repository
public class GrpcDataClient {
    private String host = "localhost";
    private int port = 6568;
    private final GrpcLoggerClient logger = new GrpcLoggerClient();

    public boolean add(LogData logData) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        AccessDBGrpc.AccessDBStub stub = AccessDBGrpc.newStub(channel);
        final CountDownLatch finishLatch = new CountDownLatch(1);

        StreamObserver<Id> responseObserver = new StreamObserver<Id>() {
            @Override
            public void onNext(Id id) {
                logger.info("Sent value to DB");
            }

            @Override
            public void onError(Throwable throwable) {
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        };
        StreamObserver<LogData> requestObserver = stub.add(responseObserver);

        try {
            requestObserver.onNext(logData);
        } catch (StatusRuntimeException ex) {
            logger.error("GRPC failed to talk to DB");
            requestObserver.onError(ex);
        }
        requestObserver.onCompleted();

        // Receiving happens asynchronously
        try{
            finishLatch.await(1, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            logger.error("Failed to write to DB");
        }

        channel.shutdown();
        return true;
    }

    public List<Uid> getUidList() {
        List<Uid> uidList = new ArrayList<>();
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        AccessDBGrpc.AccessDBBlockingStub stub = AccessDBGrpc.newBlockingStub(channel);
        Iterator<LogData> logResponse;
        try {
            logResponse = stub.getAll(Id.newBuilder().setId(10).build());
            while (logResponse.hasNext()) {
                LogData receiveData = logResponse.next();
                // convert object and only add to list if unique, create
                Uid uid = Uid.newBuilder().setUid(receiveData.getUid()).build();
                if (!uidList.contains(uid)) {
                    uidList.add(uid);
                }
            }
        } catch (StatusRuntimeException ex) {
            logger.error("GRPC failed to talk to DB");
            return uidList;
        }

        channel.shutdown();
        return uidList;
    }

    public Channel getChannel(com.cit.micro.data.Uid uid){
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();

        AccessDBGrpc.AccessDBBlockingStub stub = AccessDBGrpc.newBlockingStub(channel);
        Channel mqttChannel = stub.getChanel(uid);
        logger.info(String.format("Found Channel: %s For UID: %s",
                mqttChannel.getChannel(),
                mqttChannel.getUid()));
        channel.shutdown();

        return mqttChannel;
    }
}
