package com.eshoponcontainers.services;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.eshoponcontainers.entities.OutboxEntity;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.repositories.OutboxRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "outbox.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final EventBus eventBus;
    private final String podName;

    public OutboxProcessor(OutboxRepository outboxRepository, EventBus eventBus) {
        this.outboxRepository = outboxRepository;
        this.eventBus = eventBus;
        this.podName = System.getenv().getOrDefault("HOSTNAME", "LOCAL-WORKER");
    }

    @Scheduled(fixedDelayString = "${outbox.fixedDelay:30000}") // Run every 30 seconds if not specified
    public void processOutbox() {
        boolean hasMore = true;
        while (hasMore) {
            hasMore = processNextBatch();
        }
    }

    private boolean processNextBatch() {
        // 1. Fetch batch using our atomic SQL query
        List<OutboxEntity> outboxEvents = outboxRepository.fetchAndLockBatch(podName, 3);
        
        List<IntegrationEvent> integrationEvents = null; // Convert Outbox to IntegrationEvent using your mapping logic

        if (outboxEvents.isEmpty()) return false;

        for (IntegrationEvent event : integrationEvents) {
            try {
                eventBus.publish(event);
                // 2. On success,  mark PUBLISHED)
                outboxRepository.markAsPublished(event.getId());
            } catch (Exception e) {
                log.error("Failed to publish event {}: {}", event.getId(), e.getMessage());
                // We don't need to do anything here; 
                // the RetryCount was already incremented in the SQL query.
            }
        }
        return true; // Continue loop to check for more records immediately
    }

}
