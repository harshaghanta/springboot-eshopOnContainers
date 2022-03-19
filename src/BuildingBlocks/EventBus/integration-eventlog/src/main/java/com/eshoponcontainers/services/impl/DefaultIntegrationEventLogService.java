package com.eshoponcontainers.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.eshoponcontainers.EventStateEnum;
import com.eshoponcontainers.entities.IntegrationEventLogEntry;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.repositories.IntegrationEventLogRepository;
import com.eshoponcontainers.services.IntegrationEventLogService;

public class DefaultIntegrationEventLogService implements IntegrationEventLogService {

    IntegrationEventLogRepository eventLogRepository;
    public DefaultIntegrationEventLogService(IntegrationEventLogRepository eventLogRepository ) {
        this.eventLogRepository = eventLogRepository;
    }

    @Override
    public List<IntegrationEventLogEntry> retrieveEventLogsPendingToPublish(UUID transactionId ) {
        List<IntegrationEventLogEntry> pendingEvents = eventLogRepository.findByTransactionIdAndState(transactionId.toString(), EventStateEnum.NOT_PUBLISHED);

        if(pendingEvents != null && !pendingEvents.isEmpty()) {
           pendingEvents.sort(Comparator.comparing(IntegrationEventLogEntry::getCreationTime));
           pendingEvents.forEach(x-> x.deserializeEventContent());
           return pendingEvents;
        }

        return null;
    }

    @Override
    public void saveEvent(IntegrationEvent event, UUID transactionId) {        
        
        IntegrationEventLogEntry logEntry = new IntegrationEventLogEntry(event, transactionId);
        eventLogRepository.save(logEntry);
    }

    @Override
    public void markEventAsPublished(UUID eventId) {
        
        updateEventStatus(eventId, EventStateEnum.PUBLISHED);
    }

    @Override
    public void markEventAsInProgress(UUID eventId) {
        
        updateEventStatus(eventId, EventStateEnum.IN_PROGRESS);
    }

    @Override
    public void markEventAsFailed(UUID eventId) {
        
        updateEventStatus(eventId, EventStateEnum.PUBLISH_FAILED);
        
    }

    private void updateEventStatus(UUID eventId, EventStateEnum status) {
        Optional<IntegrationEventLogEntry> optionalEvent = eventLogRepository.findById(eventId);
        if(optionalEvent.isPresent()) {
            IntegrationEventLogEntry event = optionalEvent.get();
            event.setState(status);

            if(event.getState() == EventStateEnum.IN_PROGRESS) {
                event.setTimesSent(event.getTimesSent() + 1);
            }
            eventLogRepository.save(event);
        }
    }
}
