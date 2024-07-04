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

        try {
            log.info("PreUpdate event triggered for the entity: {}", entity.getClass().getSimpleName());
    
            captureDomainEvents(entity);
            
        } catch (Exception e) {
            log.error("Error in preUpdate event for the entity: {}", entity.getClass().getSimpleName(), e);
        }

    }

    public void postUpdate(Entity entity) {
        try {
            
            log.info("PostUpdate event triggered for the entity: {}", entity.getClass().getSimpleName());
    
            captureDomainEvents(entity);
        } catch (Exception e) {
            log.error("Error in postUpdate event for the entity: {}", entity.getClass().getSimpleName(), e);
        }
    }

    public void prePersist(Entity entity) {
        try {
            
            log.info("PrePersist event triggered for the entity: {}", entity.getClass().getSimpleName());
            captureDomainEvents(entity);
        } catch (Exception e) {
            log.error("Error in prePersist event for the entity: {}", entity.getClass().getSimpleName(), e);
        }
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
