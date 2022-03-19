package com.eshoponcontainers.catalogapi;

import javax.annotation.PostConstruct;

import com.eshoponcontainers.catalogapi.integrationevents.eventhandlers.ProductPriceChangedEventHandler;
import com.eshoponcontainers.catalogapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.eventbus.abstractions.EventBus;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartUp {

    @Autowired
    private EventBus eventBus;
    

    @PostConstruct
    public void init() {
        System.out.println("Starting catalog-api");
        eventBus.subscribe(ProductPriceChangedIntegrationEvent.class, ProductPriceChangedEventHandler.class);
    }
}
