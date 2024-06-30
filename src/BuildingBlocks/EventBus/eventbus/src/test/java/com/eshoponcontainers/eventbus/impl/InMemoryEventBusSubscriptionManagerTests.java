package com.eshoponcontainers.eventbus.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

import com.eshoponcontainers.eventbus.TestIntegrationEvent;
import com.eshoponcontainers.eventbus.TestIntegrationEventHandler;
import com.eshoponcontainers.eventbus.TestIntegrationOtherEventHandler;

public class InMemoryEventBusSubscriptionManagerTests {

    @Test
    void after_creation_should_be_empty() {
        var manager = new InMemoryEventBusSubscriptionManager();
        assertTrue(manager.isEmpty());
    }

    @Test
    void after_one_event_subscription_should_contain_the_event() {
        var manager = new InMemoryEventBusSubscriptionManager();
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        assertTrue(manager.hasSubscriptionsForEvent(TestIntegrationEvent.class));
    }

    @Test
    void after_all_subscriptions_are_deleted_event_should_no_longer_exists() {
        var manager = new InMemoryEventBusSubscriptionManager();
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        manager.removeSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        assertFalse(manager.hasSubscriptionsForEvent(TestIntegrationEvent.class));
    }

    @Test
    void deleting_last_subscription_should_raise_on_deleted_event() {
        var manager = new InMemoryEventBusSubscriptionManager();
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        manager.removeSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        //TODO: HIGH : NEED TO IMPLEMENT EVENT AFTER DELETION
        fail("Event removed event not implemented yet");
        assertTrue(manager.isEmpty());
    }

    @Test
    void get_handlers_for_event_should_return_all_handlers() {
        var manager = new InMemoryEventBusSubscriptionManager();
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationEventHandler.class);
        manager.addSubscription(TestIntegrationEvent.class, TestIntegrationOtherEventHandler.class);
        var handlers = manager.getHandlersForEvent(TestIntegrationEvent.class);
        assertEquals(2, handlers.size());

    }




}
