package com.eshoponcontainers.catalogapi.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.catalogapi.context.TransactionIdHolder;
import com.eshoponcontainers.catalogapi.entities.CatalogItem;
import com.eshoponcontainers.catalogapi.repositories.CatalogItemRepository;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.services.IntegrationEventLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CatalogIntegrationService {

    private final CatalogItemRepository catalogItemRepository;
    private final IntegrationEventLogService eventLogService;
    private final EventBus eventBus;

    @Transactional
    public void saveEventAndCatalogChanges(IntegrationEvent event, List<CatalogItem> requestedItem) {

        catalogItemRepository.saveAll(requestedItem);

        eventLogService.saveEvent(event, TransactionIdHolder.getTransactionId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void publishThroughEventBus(IntegrationEvent event) {

        try {
            log.info("----- Publishing integration event: {} from {} - ({})", event.getId(), "Catalog", event);
            eventLogService.markEventAsInProgress(event.getId());
            eventBus.publish(event);
            eventLogService.markEventAsPublished(event.getId());
        } catch (Exception e) {
            log.info("ERROR Publishing integration event: {} from {} - ({})", event.getId(), "Catalog", event);
            eventLogService.markEventAsFailed(event.getId());
        }
    }
}
