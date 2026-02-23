package com.eshoponcontainers.orderapi.application.integrationEvents;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.entities.IntegrationEventLogEntry;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.orderapi.services.TransactionContext;
import com.eshoponcontainers.services.impl.DefaultIntegrationEventLogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderingIntegrationEventService implements IOrderingIntegrationEventService {

    private final DefaultIntegrationEventLogService eventLogService;
    private final EventBus eventBus;

    @Override
    @Transactional
    public void publishEventsThroughEventBus(UUID transactionId) {

        List<IntegrationEventLogEntry> pendingLogEvents = eventLogService
                .retrieveEventLogsPendingToPublish(transactionId);
        for (IntegrationEventLogEntry logEntry : pendingLogEvents) {
            UUID eventId = logEntry.getEventId();
            log.info("----- Publishing integration event: {} from {} - ({})", eventId, "OrderAPI", logEntry);
            
            try {
                eventLogService.markEventAsInProgress(eventId);
                eventBus.publish(logEntry.getEvent());
                eventLogService.markEventAsPublished(logEntry.getEventId());
            } catch (Exception e) {
                log.error("ERROR publishing integration event: " + eventId + " from OrderAPI" , e);
                eventLogService.markEventAsFailed(eventId);
            }
        }
    }

    @Override
    public void addAndSaveEvent(IntegrationEvent event) {
        log.info("----- Enqueuing integration event {} with transaction ID {} to repository ({})", event.getId(), TransactionContext.getTransactionId(), event);
        eventLogService.saveEvent(event, TransactionContext.getTransactionId());
    }

}
