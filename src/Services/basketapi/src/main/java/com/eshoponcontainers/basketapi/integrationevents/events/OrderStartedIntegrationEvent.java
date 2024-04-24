package com.eshoponcontainers.basketapi.integrationevents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderStartedIntegrationEvent extends IntegrationEvent {

    private String userId;
    
}
