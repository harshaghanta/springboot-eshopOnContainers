package com.eshoponcontainers.catalogapi.services;

import java.util.UUID;

import javax.transaction.Transactional;

import com.eshoponcontainers.catalogapi.entities.CatalogItem;

import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.services.IntegrationEventLogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CatalogIntegrationService {

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @Autowired
    private IntegrationEventLogService eventLogService;

    @Autowired
    private EventBus eventBus;
  

    @Transactional
    public void saveEventAndCatalogChanges(IntegrationEvent event, CatalogItem requestedItem) {

        catalogItemRepository.save(requestedItem);
        //TODO : HIGH : NEED TO GET TRANSSACTION ID
        UUID transactionId = UUID.randomUUID();
        eventLogService.saveEvent(event, transactionId);
    }

    public void publishThroughEventBus(IntegrationEvent event) {

        eventLogService.markEventAsInProgress(event.getId());
        eventBus.publish(event);
        eventLogService.markEventAsPublished(event.getId());
    }
    
}
