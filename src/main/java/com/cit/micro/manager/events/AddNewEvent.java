package com.cit.micro.manager.events;

import com.cit.micro.manager.Subscribe;
import org.springframework.context.ApplicationEvent;

public class AddNewEvent extends ApplicationEvent {
    private Subscribe subscribe;

    public AddNewEvent(Object source, Subscribe subscribe){
        super(source);
        this.subscribe = subscribe;
    }

    public Subscribe getSubscribe() {
        return subscribe;
    }
}
