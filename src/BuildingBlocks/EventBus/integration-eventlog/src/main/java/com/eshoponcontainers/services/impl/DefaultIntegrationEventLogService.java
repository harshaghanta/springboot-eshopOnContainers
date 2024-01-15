package com.eshoponcontainers.services.impl;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eshoponcontainers.EventStateEnum;
import com.eshoponcontainers.entities.IntegrationEventLogEntry;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.services.IntegrationEventLogService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DefaultIntegrationEventLogService implements IntegrationEventLogService {

    // IntegrationEventLogRepository eventLogRepository;
    private final EntityManager entityManager;

    // public DefaultIntegrationEventLogService(IntegrationEventLogRepository eventLogRepository) {
    //     this.eventLogRepository = eventLogRepository;        
    // }

    // public DefaultIntegrationEventLogService(EntityManager entityManager  ) {        
    //     this.entityManager = entityManager;
    // }

    @Override
    public List<IntegrationEventLogEntry> retrieveEventLogsPendingToPublish(UUID transactionId ) {
        // List<IntegrationEventLogEntry> pendingEvents = eventLogRepository.findByTransactionIdAndState(transactionId.toString(), EventStateEnum.NOT_PUBLISHED);
        List<IntegrationEventLogEntry> pendingEvents = null;
        
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<IntegrationEventLogEntry> criteria = criteriaBuilder.createQuery(IntegrationEventLogEntry.class);
        Root<IntegrationEventLogEntry> root = criteria.from(IntegrationEventLogEntry.class);

        Predicate transactionFilter = criteriaBuilder.equal(root.get("TransactionId"), transactionId);
        Predicate eventStateFilter = criteriaBuilder.equal(root.get("state"), EventStateEnum.NOT_PUBLISHED);

        CriteriaQuery<IntegrationEventLogEntry> criteriaQuery = criteria.select(root)
            .where( criteriaBuilder.and(transactionFilter, eventStateFilter));

        pendingEvents = entityManager.createQuery(criteriaQuery).getResultList();

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
        entityManager.persist(logEntry);
        // eventLogRepository.save(logEntry);
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
        // Optional<IntegrationEventLogEntry> optionalEventLogEntry = eventLogRepository.findById(eventId);
        IntegrationEventLogEntry eventLogEntry = entityManager.find(IntegrationEventLogEntry.class, eventId);
        // if(optionalEventLogEntry.isPresent()) {
        if(eventLogEntry != null) {
            // IntegrationEventLogEntry eventLogEntry = optionalEventLogEntry.get();
            
            eventLogEntry.setState(status);

            if(eventLogEntry.getState() == EventStateEnum.IN_PROGRESS) {
                eventLogEntry.setTimesSent(eventLogEntry.getTimesSent() + 1);
            }
            // eventLogRepository.save(event);
            entityManager.merge(eventLogEntry);
        }
    }
}
