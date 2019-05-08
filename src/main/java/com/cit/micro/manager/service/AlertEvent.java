package com.cit.micro.manager.service;

import com.cit.micro.data.LogData;
import org.springframework.context.ApplicationEvent;

public class AlertEvent extends ApplicationEvent {
    private LogData logData;

    public AlertEvent(Object source, LogData logData){
        super(source);
        this.logData = logData;
    }

    public LogData getLogData(){
        return this.logData;
    }
}
