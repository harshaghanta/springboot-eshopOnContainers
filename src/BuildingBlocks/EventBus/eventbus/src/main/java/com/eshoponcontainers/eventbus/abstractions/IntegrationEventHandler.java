package com.eshoponcontainers.eventbus.abstractions;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

public interface IntegrationEventHandler<T extends IntegrationEvent> {

    Runnable handle(T event);
}
