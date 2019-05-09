package com.cit.micro.manager.service;

import com.cit.micro.data.Channel;
import com.cit.micro.manager.Uid;
import com.cit.micro.manager.client.GrpcDataClient;
import com.cit.micro.manager.client.GrpcLoggerClient;
import com.cit.micro.manager.events.AddNewEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService implements ApplicationListener<AddNewEvent> {

    private static final GrpcLoggerClient log = new GrpcLoggerClient();
    private static GrpcDataClient grpcDataClient;
    private static ApplicationEventPublisher applicationEventPublisher;
    private static List<Uid> uids =  new ArrayList<>();
    private static List<Channel> channels = new ArrayList<>();
    private static List<SubscriberService> subscriberServices = new ArrayList<>();

    @Autowired
    public ManagerService(GrpcDataClient grpcDataClientInstance,
                          ApplicationEventPublisher applicationEventPublisherInstance) {
        grpcDataClient = grpcDataClientInstance;
        applicationEventPublisher = applicationEventPublisherInstance;
    }

    private static void populateUids(){
        uids = grpcDataClient.getUidList();
    }

    private static void populateChannels(){
        for (Uid managerUid: uids
             ) {
            com.cit.micro.data.Uid dataUid = com.cit.micro.data.Uid.newBuilder().setUid(managerUid.getUid()).build();
            channels.add(grpcDataClient.getChannel(dataUid));
        }
    }

    public static void subscribeToChannels(){
        populateUids();
        populateChannels();
        for (Channel channel: channels
             ) {
            log.info(String.format("Subscribing to channel: %s for UID: %s", channel.getChannel(), channel.getUid()));
            SubscriberService subscriberService = new SubscriberService(applicationEventPublisher);
            subscriberService.subscribe(channel.getChannel(), channel.getUid());
            subscriberServices.add(subscriberService);
        }
    }

    @Override
    public void onApplicationEvent(AddNewEvent event){
        SubscriberService subscriberService = new SubscriberService(applicationEventPublisher);
        subscriberService.subscribe(event.getSubscribe().getChannel(),
                event.getSubscribe().getUid());
        subscriberServices.add(subscriberService);
    }
}
