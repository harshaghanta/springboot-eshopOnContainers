package com.eshoponcontainers.catalogapi.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.services.IntegrationEventLogService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CatalogIntegrationService {

    
    private final CatalogItemRepository catalogItemRepository;    
    private final IntegrationEventLogService eventLogService;
    private final EventBus eventBus;  

    @Transactional
    public void saveEventAndCatalogChanges(IntegrationEvent event, List<CatalogItem> requestedItem) {

        catalogItemRepository.saveAll(requestedItem);
        //TODO : HIGH : NEED TO GET TRANSSACTION ID
        UUID transactionId = UUID.randomUUID();
        eventLogService.saveEvent(event, transactionId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishThroughEventBus(IntegrationEvent event) {

        eventLogService.markEventAsInProgress(event.getId());
        eventBus.publish(event);
        eventLogService.markEventAsPublished(event.getId());
    }
    
}
