package com.eshoponcontainers.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eshoponcontainers.entities.OutboxEntity;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.repositories.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@ConditionalOnProperty(name = "outbox.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class OutboxProcessor {

    private final OutboxRepository outboxRepository;
    private final EventBus eventBus;
    private final String podName;
    private final String appName;
    private final ObjectMapper objectMapper;

    public OutboxProcessor(OutboxRepository outboxRepository, Environment env,  EventBus eventBus, ObjectMapper objectMapper) {
        this.outboxRepository = outboxRepository;
        this.eventBus = eventBus;
        this.podName = env.getProperty("HOSTNAME", "LOCAL-WORKER");
        this.appName = env.getProperty("spring.application.name", "LOCAL-APP");
        this.objectMapper = objectMapper;
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
        List<OutboxEntity> outboxEvents = outboxRepository.fetchAndLockBatch(podName, 3, appName);

        List<IntegrationEvent> integrationEvents = null; // Convert Outbox to IntegrationEvent using your mapping logic

        if (outboxEvents.isEmpty()) {
            log.info("No outstanding outbox events");
            return false;
        }

        integrationEvents = convertToIntegrationEvents(outboxEvents);

        for (IntegrationEvent event : integrationEvents) {
            try {
                eventBus.publish(event);
                // 2. On success, mark PUBLISHED)
                outboxRepository.markAsPublished(event.getId());
                log.info("Published event: {} from outbox", event);
            } catch (Exception e) {
                log.error("Failed to publish event {}: {}", event.getId(), e.getMessage());
                // We don't need to do anything here;
                // the RetryCount was already incremented in the SQL query.
            }
        }
        return true; // Continue loop to check for more records immediately
    }

    private List<IntegrationEvent> convertToIntegrationEvents(List<OutboxEntity> outboxEvents) {

        return outboxEvents.stream().map(outboxevent -> {
            try {
                Class<?> clazz = Class.forName(outboxevent.getEventTypeName());
                return (IntegrationEvent) objectMapper.readValue(outboxevent.getContent(), clazz);
            } catch (Exception e) {
                log.error("Failed to deserialize event: {}", e.getMessage());
                return null;
            }
        }).filter(e -> e != null).collect(Collectors.toList());
    }

}
