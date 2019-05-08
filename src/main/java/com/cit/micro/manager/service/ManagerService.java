package com.cit.micro.manager.service;

import com.cit.micro.data.Channel;
import com.cit.micro.manager.Uid;
import com.cit.micro.manager.client.GrpcDataClient;
import com.cit.micro.manager.client.GrpcLoggerClient;
import com.cit.micro.manager.service.SubscriberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService {

    private static final GrpcLoggerClient log = new GrpcLoggerClient();
    private static GrpcDataClient grpcDataClient;
    private static SubscriberService subscriberService;

    @Autowired
    public ManagerService(GrpcDataClient grpcDataClientInstance,
                          SubscriberService subscriberServiceInstance) {
        subscriberService = subscriberServiceInstance;
        grpcDataClient = grpcDataClientInstance;
    }

    private static List<Uid> uids =  new ArrayList<>();
    private static List<Channel> channels = new ArrayList<>();

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
            log.info(String.format("Subscribing to channel %s", channel.getChannel()));
            subscriberService.subscribe(channel.getChannel());
        }
    }

    //todo have event when added a new system??

}
