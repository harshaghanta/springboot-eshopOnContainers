package com.eshoponcontainers.catalogapi.integrationevents.eventhandlers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusChangedToPaidIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    private final CatalogItemRepository catalogItemRepository;

    @Override
    public Runnable handle(OrderStatusChangedToPaidIntegrationEvent event) {
        Runnable runnable = () -> {
            log.info("----- Handling integration event: {} at {AppName} - ({})", event.getId(), "Catalog", event);
            var catItems = new ArrayList<CatalogItem>();

            for (OrderStatusChangedToPaidIntegrationEvent.OrderStockItem item : event.getItems()) {
                Optional<CatalogItem> optCatalogItem = catalogItemRepository.findById(item.getProductId());
                if (optCatalogItem.isPresent()) {
                    catItems.add(optCatalogItem.get());
                    optCatalogItem.get().removeStock(item.getUnits());
                }
            }
            catalogItemRepository.saveAll(catItems);
        };
        
        return runnable;
    }

}
