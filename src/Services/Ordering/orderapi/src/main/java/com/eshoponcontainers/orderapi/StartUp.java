package com.eshoponcontainers.orderapi;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers.GracePeriodConfirmedIntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.GracePeriodConfirmedIntegrationEvent;

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
        log.info("Subscribing to the Integration Events in OrderAPI");
        //
        //eventBus.subscribe(ProductPriceChangedIntegrationEvent.class, ProductPriceChangedEventHandler.class);
        eventBus.subscribe(GracePeriodConfirmedIntegrationEvent.class, GracePeriodConfirmedIntegrationEventHandler.class);
    }
}
