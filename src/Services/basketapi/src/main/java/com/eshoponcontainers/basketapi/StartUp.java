package com.eshoponcontainers.basketapi;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

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
        log.info("Starting basketapi");
        // eventBus.subscribe(ProductPriceChangedIntegrationEvent.class, ProductPriceChangedEventHandler.class);
    }
}
