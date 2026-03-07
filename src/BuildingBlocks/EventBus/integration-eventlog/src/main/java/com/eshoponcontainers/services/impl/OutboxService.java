package com.eshoponcontainers.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.eshoponcontainers.entities.OutboxEntity;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.repositories.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@ConditionalOnProperty(prefix = "outbox", name = "enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final Environment env;

    public void saveToOutbox(IntegrationEvent event) {

        // Convert from IntegrationEvent to OutboxEntity and save to DB

        String content = null;
        try {
            content = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }

        LocalDateTime creationDateTime = event.getCreationDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        OutboxEntity entry = new OutboxEntity(event.getId(), event.getClass().getName(), content, "PENDING",
                env.getProperty("HOSTNAME"), creationDateTime, 0, env.getProperty("spring.application.name"));
        outboxRepository.save(entry);
    }
}
