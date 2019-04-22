package com.cit.micro.manager.service;

import com.cit.micro.data.LogData;
import com.cit.micro.manager.*;
import com.cit.micro.manager.client.GrpcDataClient;
import com.cit.micro.manager.client.GrpcLoggerClient;
import io.grpc.stub.StreamObserver;

import java.util.List;

public class GrpcManager extends AccessManagerGrpc.AccessManagerImplBase {
    private final GrpcLoggerClient logger = new GrpcLoggerClient();
    private final GrpcDataClient dataClient = new GrpcDataClient();

    @Override
    public void getUid(Id request, StreamObserver<Uid> responseObserver) {
        logger.info("Getting list of UID");

        List<Uid> uidList = dataClient.getUidList();
        for (Uid uid: uidList
        ) {
            responseObserver.onNext(uid);
        }
        responseObserver.onCompleted();
    }

    @Override
    public void subscribeNew(Subscribe request, StreamObserver<Result> responseObserver) {
        logger.info("Adding new Subscription");
        // todo add new subscription
        logger.info(request.getUid());
        LogData logData = LogData.newBuilder().setUid(request.getUid()).setChannel(request.getChannel()).setText("{\"null\":\"null\"}").build();

        Result response = Result.newBuilder().setSuccess(dataClient.add(logData)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
