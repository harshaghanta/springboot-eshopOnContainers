package com.eshoponcontainers.catalogapi;

import org.springframework.stereotype.Component;

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
        System.out.println("Starting catalog-api");
        log.info("Subscribing to the Integration Events");
        //
        //eventBus.subscribe(ProductPriceChangedIntegrationEvent.class, ProductPriceChangedEventHandler.class);
    }
}
