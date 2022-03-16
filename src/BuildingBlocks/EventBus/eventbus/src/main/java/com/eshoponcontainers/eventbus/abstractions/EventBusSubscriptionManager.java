package com.eshoponcontainers.eventbus.abstractions;

import java.util.List;

import com.eshoponcontainers.eventbus.SubscriptionInfo;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;


public interface EventBusSubscriptionManager {

    <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void addSubscription(Class<T> eventType, Class<TH> eventHandlerType);

    <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void removeSubscription();

    <T extends IntegrationEvent> String getEventKey(Class<T> eventType);

    <T extends IntegrationEvent> boolean hasSubscriptionsForEvent(Class<T> eventType);

    boolean hasSubscriptionsForEvent(String eventName);

    Class getEventTypeByName(String eventName);

    <T extends IntegrationEvent> List<SubscriptionInfo> getHandlersForEvent(Class<T> eventType);
    
}
