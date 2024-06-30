package com.eshoponcontainers.eventbus;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;

public class TestIntegrationOtherEventHandler implements IntegrationEventHandler<TestIntegrationEvent> {

    @Override
    public Runnable handle(TestIntegrationEvent event) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handle'");
    }

}
