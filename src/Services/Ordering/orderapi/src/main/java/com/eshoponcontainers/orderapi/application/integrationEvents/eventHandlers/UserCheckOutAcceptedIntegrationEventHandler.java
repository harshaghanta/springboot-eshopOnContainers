package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.UserCheckoutAcceptedIntegrationEvent;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCheckOutAcceptedIntegrationEventHandler
        implements IntegrationEventHandler<UserCheckoutAcceptedIntegrationEvent> {

    private final Pipeline pipeline;

    @Override
    public Runnable handle(UserCheckoutAcceptedIntegrationEvent event) {
        Runnable runnable = () -> {
            if (event.getRequestId() != null) {
                var command = new CreateOrderCommand(null, null, null, null, null, null, null, null, null, null, null,
                        null, 0);
                Boolean orderCreated = pipeline.send(command);
                if (orderCreated) {
                    log.info("----- CreateOrderCommand suceeded - RequestId: {}", event.getRequestId());
                } else {
                    log.warn("CreateOrderCommand failed - RequestId: {}", event.getRequestId());
                }
            }
            else {
                log.warn("Invalid IntegrationEvent - RequestId is missing - {}", event);
            }
        };
        runnable.run();
        return runnable;
    }

}
