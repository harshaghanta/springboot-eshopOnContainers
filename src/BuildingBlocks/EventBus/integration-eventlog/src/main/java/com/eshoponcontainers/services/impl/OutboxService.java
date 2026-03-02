package com.eshoponcontainers.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import com.eshoponcontainers.entities.OutboxEntity;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.eshoponcontainers.repositories.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public void saveToOutbox(IntegrationEvent event) {

        // Convert from IntegrationEvent to OutboxEntity and save to DB

        String content = null;
        try {
            content = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        LocalDateTime creationDateTime = event.getCreationDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        OutboxEntity entry = new OutboxEntity(event.getId(), event.getClass().getName(), content, "PENDING",
                creationDateTime, 0);
        outboxRepository.save(entry);
    }
}
