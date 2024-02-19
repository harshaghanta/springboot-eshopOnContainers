package com.eshoponcontainers.basketapi;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.basketapi.integrationevents.eventhandlers.OrderStartedIntegrationEventHandler;
import com.eshoponcontainers.basketapi.integrationevents.eventhandlers.ProductPriceChangedEventHandler;
import com.eshoponcontainers.basketapi.integrationevents.events.OrderStartedIntegrationEvent;
import com.eshoponcontainers.basketapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.eventbus.abstractions.EventBus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartUp {
    
    private final EventBus eventBus;    

    @PostConstruct
    public void init() {
        log.info("Subscribing to the Integration Events in BasketAPI");
        eventBus.subscribe(ProductPriceChangedIntegrationEvent.class, ProductPriceChangedEventHandler.class);
        eventBus.subscribe(OrderStartedIntegrationEvent.class, OrderStartedIntegrationEventHandler.class);
    }
}
