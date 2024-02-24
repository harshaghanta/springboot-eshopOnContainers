package com.eshoponcontainers.listeners;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.seedWork.Entity;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EntityEventListener {

    public void preUpdate(Entity entity) {
        log.info("PreUpdate event triggered for the entity: {}", entity.getClass().getSimpleName());

        captureDomainEvents(entity);

    }

    public void postUpdate(Entity entity) {
        log.info("PostUpdate event triggered for the entity: {}", entity.getClass().getSimpleName());

        captureDomainEvents(entity);
    }

    public void prePersist(Entity entity) {
        log.info("PrePersist event triggered for the entity: {}", entity.getClass().getSimpleName());
        captureDomainEvents(entity);
    }

    private void captureDomainEvents(Entity entity) {
        String delimitedDomainEvents = entity.getDomainEvents().stream()
                .map(Object::getClass)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(","));

        log.info("Received the domainevents : [{}] for the entity: {}", delimitedDomainEvents,
                entity.getClass().getSimpleName());

        if (entity.getDomainEvents() != null)
            DomainContext.addDomainEvents(entity.getDomainEvents());

        entity.clearDomainEvents();
    }
}
