package com.eshoponcontainers.orderingnotification;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers.OrderStatusChangedToAwaitingValidationIntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers.OrderStatusChangedToCancelledIntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers.OrderStatusChangedToPaidIntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers.OrderStatusChangedToShippedIntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers.OrderStatusChangedToStockConfirmedIntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.eventHandlers.OrderStatusChangedToSubmittedIntegrationEventHandler;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToCancelledIntegrationEvent;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToShippedIntegrationEvent;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;
import com.eshoponcontainers.orderingnotification.integrationEvents.events.OrderStatusChangedToSubmittedIntegrationEvent;

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
        log.info("Subscribing to the Integration Events in OrderNotfication");

        eventBus.subscribe(OrderStatusChangedToAwaitingValidationIntegrationEvent.class, OrderStatusChangedToAwaitingValidationIntegrationEventHandler.class);
        eventBus.subscribe(OrderStatusChangedToPaidIntegrationEvent.class, OrderStatusChangedToPaidIntegrationEventHandler.class);
        eventBus.subscribe(OrderStatusChangedToStockConfirmedIntegrationEvent.class, OrderStatusChangedToStockConfirmedIntegrationEventHandler.class);
        eventBus.subscribe(OrderStatusChangedToShippedIntegrationEvent.class, OrderStatusChangedToShippedIntegrationEventHandler.class);
        eventBus.subscribe(OrderStatusChangedToCancelledIntegrationEvent.class, OrderStatusChangedToCancelledIntegrationEventHandler.class);
        eventBus.subscribe(OrderStatusChangedToSubmittedIntegrationEvent.class, OrderStatusChangedToSubmittedIntegrationEventHandler.class);
    }
}
