package com.eshoponcontainers.webhooksapi.integrationevents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.webhooksapi.integrationevents.events.ProductPriceChangedIntegrationEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductPriceChangedIntegrationEventHandler implements IntegrationEventHandler<ProductPriceChangedIntegrationEvent> {

    @Override
    public Runnable handle(ProductPriceChangedIntegrationEvent event) {
        
        Runnable runnable = () -> {
            log.info("ProductPriceChangeIntegration event: ProductId: {}, OldPrice: {}, NewPrice: {}", event.getProductId(), event.getOldPrice(), event.getNewPrice());
        };
        runnable.run();
        return runnable;
        
    }

}
