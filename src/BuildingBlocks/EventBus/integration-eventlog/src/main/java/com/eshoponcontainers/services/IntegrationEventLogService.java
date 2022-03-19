package com.eshoponcontainers.services;

import java.util.List;
import java.util.UUID;

import com.eshoponcontainers.entities.IntegrationEventLogEntry;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;

public interface IntegrationEventLogService {
    
    List<IntegrationEventLogEntry> retrieveEventLogsPendingToPublish(UUID transactionId);

    void saveEvent(IntegrationEvent event, UUID transactionId);

    void markEventAsPublished(UUID eventId);

    void markEventAsInProgress(UUID eventId);

    void markEventAsFailed(UUID eventId);
}
