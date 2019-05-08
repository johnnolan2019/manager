package com.cit.micro.manager.service;

import com.cit.micro.data.Channel;
import com.cit.micro.manager.Uid;
import com.cit.micro.manager.client.GrpcDataClient;
import com.cit.micro.manager.client.GrpcLoggerClient;
import com.cit.micro.manager.service.SubscriberService;

import java.util.ArrayList;
import java.util.List;

public class ManagerService {

    private final GrpcLoggerClient log = new GrpcLoggerClient();
    private GrpcDataClient grpcDataClient;
    private SubscriberService subscriberService;
    private ListenerService listenerService;

    private List<Uid> uids =  new ArrayList<>();
    private List<Channel> channels = new ArrayList<>();

    private void populateUids(){
        uids = grpcDataClient.getUidList();
    }

    private void populateChannels(){
        for (Uid managerUid: uids
             ) {
            com.cit.micro.data.Uid dataUid = com.cit.micro.data.Uid.newBuilder().setUid(managerUid.getUid()).build();
            channels.add(grpcDataClient.getChannel(dataUid));
        }

    }

    public void subscribeToChannel(){
        populateUids();
        populateChannels();
        for (Channel channel: channels
             ) {
            subscriberService.subscribe(channel.getChannel());
        }
    }

    //todo have event when added a new system??

}
