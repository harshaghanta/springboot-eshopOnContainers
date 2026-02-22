package com.eshoponcontainers.eventbus.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.reflections.Reflections;

import com.eshoponcontainers.eventbus.SubscriptionInfo;
import com.eshoponcontainers.eventbus.abstractions.EventBusSubscriptionManager;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryEventBusSubscriptionManager implements EventBusSubscriptionManager {

    HashMap<String, List<SubscriptionInfo>> handlers = new HashMap<>();
    private ArrayList<Class> subscribedEventTypes = new ArrayList<>();
    private ArrayList<Class> allEventTypes = new ArrayList<>();

    public InMemoryEventBusSubscriptionManager() {
        log.info("Initializing eventTypes using Reflection");

        Reflections reflections = new Reflections("com.eshoponcontainers");
        allEventTypes = new ArrayList<>(reflections.getSubTypesOf(IntegrationEvent.class));

        if(allEventTypes == null || allEventTypes.isEmpty())
        log.warn("No event types found in the specified package.");

        for (Class<? extends IntegrationEvent> eventType : allEventTypes) {
            log.info("EventType found: {}", eventType.getSimpleName());
        }
    }

    @Override
    public <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void addSubscription(Class<T> eventType,
            Class<TH> eventHandlerType) {

        String eventName = getEventKey(eventType);

        doAddSubscription(eventHandlerType, eventName, false);

        if (!subscribedEventTypes.contains(eventType)) {
            log.info("Adding event type {} to subscribedEventTypes list", eventType.getName());
            subscribedEventTypes.add(eventType);
        }
            

    }

    
    @Override
    public List<Class> getAllIntegrationEventTypes() {
        return allEventTypes;
    }

    private void doAddSubscription(Class handlerType, String eventName, Boolean isDynamic) {

        if (!hasSubscriptionsForEvent(eventName)) {
            handlers.put(eventName, new ArrayList<>());
        }

        if (!handlers.get(eventName).isEmpty()) {
            // TODO: need to throw exception
        }

        if (!isDynamic)
            handlers.get(eventName).add(SubscriptionInfo.typed(handlerType));

    }

    @Override
    public <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void removeSubscription(
            Class<T> eventType, Class<TH> eventhandlerType) {

        if (hasSubscriptionsForEvent(eventType)) {
            String eventName = getEventKey(eventType);
            handlers.remove(eventName);
            subscribedEventTypes.remove(eventType);
        }

    }

    @Override
    public <T extends IntegrationEvent> String getEventKey(Class<T> eventType) {

        return eventType.getSimpleName();
    }

    @Override
    public <T extends IntegrationEvent> boolean hasSubscriptionsForEvent(Class<T> eventType) {
        String eventName = getEventKey(eventType);
        return hasSubscriptionsForEvent(eventName);
    }

    @Override
    public boolean hasSubscriptionsForEvent(String eventName) {
        // NOTE : Need to Identify why below line is not used instead
        // return !handlers.get(eventName).isEmpty();
        return handlers.containsKey(eventName);

    }

    @Override
    public Class getEventTypeByName(String eventName) {

        Class eventType = subscribedEventTypes.stream().filter(x -> x.getSimpleName().equals(eventName)).findAny().orElse(null);
        return eventType;
    }

    @Override
    public <T extends IntegrationEvent> List<SubscriptionInfo> getHandlersForEvent(Class<T> eventType) {

        String eventName = getEventKey(eventType);

        return handlers.get(eventName);
    }

    @Override
    public HashMap<String, List<SubscriptionInfo>> getAllSubscriptions() {
        return handlers;
    }

    @Override
    public boolean isEmpty() {
        return handlers.isEmpty();
    }
}
