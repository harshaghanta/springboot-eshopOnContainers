package com.eshoponcontainers.eventbus.abstractions;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

public interface EventBus {
    
    void publish(IntegrationEvent event);

    <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void subscribe(Class<T> t, Class<TH> th);

    <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void unsubscribe(Class<T> t, Class<TH> th);
}
