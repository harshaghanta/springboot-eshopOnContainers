package com.eshoponcontainers.eventbus.abstractions;

import java.util.List;
import java.util.HashMap;

import com.eshoponcontainers.eventbus.SubscriptionInfo;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;


public interface EventBusSubscriptionManager {

    <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void addSubscription(Class<T> eventType, Class<TH> eventHandlerType);

    <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void removeSubscription(Class<T> eventType, Class<TH> eventhandlerType);

    <T extends IntegrationEvent> String getEventKey(Class<T> eventType);

    <T extends IntegrationEvent> boolean hasSubscriptionsForEvent(Class<T> eventType);

    boolean hasSubscriptionsForEvent(String eventName);

    Class getEventTypeByName(String eventName);

    <T extends IntegrationEvent> List<SubscriptionInfo> getHandlersForEvent(Class<T> eventType);

    HashMap<String, List<SubscriptionInfo>> getAllSubscriptions();

    boolean isEmpty();
    
}
