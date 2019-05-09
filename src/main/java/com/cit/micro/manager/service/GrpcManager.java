package com.cit.micro.manager.service;

import com.cit.micro.data.LogData;
import com.cit.micro.manager.*;
import com.cit.micro.manager.client.GrpcDataClient;
import com.cit.micro.manager.client.GrpcLoggerClient;
import com.cit.micro.manager.events.AddNewEvent;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrpcManager extends AccessManagerGrpc.AccessManagerImplBase {
    private final GrpcLoggerClient logger = new GrpcLoggerClient();
    private final GrpcDataClient dataClient = new GrpcDataClient();
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public GrpcManager(ApplicationEventPublisher applicationEventPublisherInstance){
        applicationEventPublisher = applicationEventPublisherInstance;
    }

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
        logger.info(request.getUid());
        LogData logData = LogData.newBuilder()
                .setUid(request.getUid())
                .setChannel(request.getChannel())
                .setText("{\"null\":\"null\"}")
                .build();
        Result response = Result.newBuilder().setSuccess(dataClient.add(logData)).build();
        if (response.getSuccess()){
            logger.info("Added a new Subscription, sending event");
            logger.info(request.getChannel());
            logger.info(request.getUid());
            AddNewEvent addNewEvent = new AddNewEvent(this, request);
            applicationEventPublisher.publishEvent(addNewEvent);
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

}
