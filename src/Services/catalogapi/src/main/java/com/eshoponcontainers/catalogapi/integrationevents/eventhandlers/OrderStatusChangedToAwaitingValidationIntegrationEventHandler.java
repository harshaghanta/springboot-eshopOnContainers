package com.eshoponcontainers.catalogapi.integrationevents.eventhandlers;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.integrationevents.events.ConfirmedOrderStockItem;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStatusChangedToAwaitingValidationIntegrationEvent;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStockConfirmedIntegrationEvent;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStockItem;
import com.eshoponcontainers.catalogapi.integrationevents.events.OrderStockRejectedIntegrationEvent;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.catalogapi.services.CatalogIntegrationService;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusChangedToAwaitingValidationIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToAwaitingValidationIntegrationEvent> {

    private final CatalogIntegrationService catalogIntegrationService;
    private final CatalogItemRepository catalogItemRepository;

    @Override
    public Runnable handle(OrderStatusChangedToAwaitingValidationIntegrationEvent event) {
        Runnable runnable = () -> {
            log.info("----- Handling integration event: {} at {} - ({})", event.getId(), "Catalog", event);
            var confirmedOrderStockItems = new ArrayList<ConfirmedOrderStockItem>();
            var catItems = new ArrayList<CatalogItem>();

            for (OrderStockItem item : event.getOrderStockItems()) {
                Optional<CatalogItem> optCatalogItem = catalogItemRepository.findById(item.getProductId());
                if (optCatalogItem.isPresent()) {

                    var catalogItem = optCatalogItem.get();
                    catItems.add(catalogItem);
                    boolean hasStock = catalogItem.getAvailableStock() >= item.getUnits();
                    ConfirmedOrderStockItem confirmedOrderStockItem = new ConfirmedOrderStockItem(catalogItem.getId(),
                            hasStock);

                    confirmedOrderStockItems.add(confirmedOrderStockItem);
                }
            }

            IntegrationEvent integrationEvent = null;
            if (confirmedOrderStockItems.stream().anyMatch(i -> !i.isHasStock())) {
                integrationEvent = new OrderStockRejectedIntegrationEvent(event.getOrderId(), confirmedOrderStockItems);
            } else
                integrationEvent = new OrderStockConfirmedIntegrationEvent(event.getOrderId());

            catalogIntegrationService.saveEventAndCatalogChanges(integrationEvent, catItems);
            catalogIntegrationService.publishThroughEventBus(integrationEvent);
        };

        return runnable;
    }
}
