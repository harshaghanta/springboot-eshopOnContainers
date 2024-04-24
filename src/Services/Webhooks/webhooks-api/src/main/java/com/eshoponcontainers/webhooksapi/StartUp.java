package com.eshoponcontainers.webhooksapi;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.webhooksapi.integrationevents.eventHandlers.OrderStatusChangedToPaidIntegrationEventHandler;
import com.eshoponcontainers.webhooksapi.integrationevents.eventHandlers.OrderStatusChangedToShippedIntegrationEventHandler;
import com.eshoponcontainers.webhooksapi.integrationevents.eventHandlers.ProductPriceChangedIntegrationEventHandler;
import com.eshoponcontainers.webhooksapi.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshoponcontainers.webhooksapi.integrationevents.events.OrderStatusChangedToShippedIntegrationEvent;
import com.eshoponcontainers.webhooksapi.integrationevents.events.ProductPriceChangedIntegrationEvent;

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
        eventBus.subscribe(OrderStatusChangedToShippedIntegrationEvent.class,
                OrderStatusChangedToShippedIntegrationEventHandler.class);
        eventBus.subscribe(ProductPriceChangedIntegrationEvent.class,
                ProductPriceChangedIntegrationEventHandler.class);

    }
}
