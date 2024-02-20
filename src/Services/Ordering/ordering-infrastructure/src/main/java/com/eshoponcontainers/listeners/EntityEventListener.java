package com.eshoponcontainers.listeners;

import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.seedWork.Entity;

public class EntityEventListener {

    public void preUpdate(Entity entity) {
        if(entity.getDomainEvents() != null)
            DomainContext.addDomainEvents(entity.getDomainEvents());

        entity.clearDomainEvents();
        
    }
}
