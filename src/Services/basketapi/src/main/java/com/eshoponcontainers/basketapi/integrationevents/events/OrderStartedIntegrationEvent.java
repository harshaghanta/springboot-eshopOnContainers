package com.eshoponcontainers.basketapi.integrationevents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.Getter;
@Getter
public class OrderStartedIntegrationEvent extends IntegrationEvent {

    private String userId;
    
    public OrderStartedIntegrationEvent(String userId) {
        this.userId = userId;
    }
    
}
