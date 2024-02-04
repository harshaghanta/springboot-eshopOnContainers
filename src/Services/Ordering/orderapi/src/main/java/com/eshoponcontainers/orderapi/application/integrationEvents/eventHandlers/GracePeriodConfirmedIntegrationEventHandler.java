package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.SetAwaitingValidationOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.GracePeriodConfirmedIntegrationEvent;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GracePeriodConfirmedIntegrationEventHandler implements IntegrationEventHandler<GracePeriodConfirmedIntegrationEvent> {

    private final Pipeline pipeline;

    @Override
    public Runnable handle(GracePeriodConfirmedIntegrationEvent event) {
        SetAwaitingValidationOrderStatusCommand command = new SetAwaitingValidationOrderStatusCommand(event.getOrderId());
        Runnable task = () -> pipeline.send(command);
        task.run();
        return task;
    }

}
