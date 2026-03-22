package com.eshoponcontainers.catalogapi.integrationevents.eventhandlers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStatusChangedToPaidIntegrationEvent;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStockItem;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.entities.InboxMessage;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.repositories.InboxRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusChangedToPaidIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToPaidIntegrationEvent> {

    private final CatalogItemRepository catalogItemRepository;
    private final InboxRepository inboxRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OrderStatusChangedToPaidIntegrationEvent event) {

        if(inboxRepository.existsById(event.getId()))
        {
            log.info("Event with id {} already processed. Skipping.", event.getId());
            return;
        }

        log.info("----- Handling integration event: {} at {} - ({})", event.getId(), "Catalog", event);
        var catItems = new ArrayList<CatalogItem>();

        for (OrderStockItem item : event.getOrderStockItems()) {
            Optional<CatalogItem> optCatalogItem = catalogItemRepository.findById(item.getProductId());
            if (optCatalogItem.isPresent()) {
                catItems.add(optCatalogItem.get());
                optCatalogItem.get().removeStock(item.getUnits());
            }
        }
        catalogItemRepository.saveAll(catItems);
        inboxRepository.save(new InboxMessage(event.getId(), event.getClass().getName()));

    }
}
