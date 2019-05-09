package com.cit.micro.manager.events;

import com.cit.micro.data.LogData;
import org.springframework.context.ApplicationEvent;

public class LogDataEvent extends ApplicationEvent {
    private LogData logData;

    public LogDataEvent(Object source, LogData logData){
        super(source);
        this.logData = logData;
    }

    public LogData getLogData(){
        return this.logData;
    }
}
