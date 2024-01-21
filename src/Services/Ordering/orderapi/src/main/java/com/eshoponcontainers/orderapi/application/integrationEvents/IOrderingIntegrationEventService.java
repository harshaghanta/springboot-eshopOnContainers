package com.eshoponcontainers.orderapi.application.integrationEvents;

import java.util.UUID;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

public interface IOrderingIntegrationEventService {

    void publishEventsThroughEventBus(UUID transactionId);

    void addAndSaveEvent(IntegrationEvent event, UUID transactionId);
}
