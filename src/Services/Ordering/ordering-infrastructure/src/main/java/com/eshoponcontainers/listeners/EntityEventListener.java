package com.eshoponcontainers.listeners;

import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.seedWork.Entity;

public class EntityEventListener {

    public void preUpdate(Entity entity) {
        DomainContext.addDomainEvents(entity.getDomainEvents());
    }
}
