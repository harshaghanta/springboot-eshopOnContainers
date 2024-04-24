package com.eshoponcontainers.paymentapi;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.paymentapi.integrationEvents.eventHandlers.OrderStatusChangedToStockConfirmedIntegrationEventHandler;
import com.eshoponcontainers.paymentapi.integrationEvents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class StartUp {

    private final EventBus eventBus;

    @PostConstruct
    public void init() {
        eventBus.subscribe(OrderStatusChangedToStockConfirmedIntegrationEvent.class, OrderStatusChangedToStockConfirmedIntegrationEventHandler.class);
    }

}
