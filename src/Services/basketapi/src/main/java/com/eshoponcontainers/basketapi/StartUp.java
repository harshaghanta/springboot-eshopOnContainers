package com.eshoponcontainers.basketapi;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.basketapi.integrationevents.eventhandlers.ProductPriceChangedEventHandler;
import com.eshoponcontainers.basketapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.eventbus.abstractions.EventBus;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StartUp {

    @Autowired
    private EventBus eventBus;    

    @PostConstruct
    public void init() {
        log.info("Starting basketapi");
        eventBus.subscribe(ProductPriceChangedIntegrationEvent.class, ProductPriceChangedEventHandler.class);
    }
}
