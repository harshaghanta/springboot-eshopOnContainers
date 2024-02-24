package com.eshoponcontainers.catalogapi;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.catalogapi.integrationevents.eventhandlers.OrderStatusChangedToAwaitingValidationIntegrationEventHandler;
import com.eshoponcontainers.catalogapi.integrationevents.eventhandlers.OrderStatusChangedToPaidIntegrationEventHandler;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshoponcontainers.eventbus.abstractions.EventBus;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartUp {

    private final EventBus eventBus;

    @PostConstruct
    public void init() {

        log.info("Subscribing to the Integration Events");

        eventBus.subscribe(OrderStatusChangedToPaidIntegrationEvent.class,
                OrderStatusChangedToPaidIntegrationEventHandler.class);
        eventBus.subscribe(OrderStatusChangedToAwaitingValidationIntegrationEvent.class,
                OrderStatusChangedToAwaitingValidationIntegrationEventHandler.class);

    }
}
