package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.SetAwaitingValidationOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.GracePeriodConfirmedIntegrationEvent;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class GracePeriodConfirmedIntegrationEventHandler implements IntegrationEventHandler<GracePeriodConfirmedIntegrationEvent> {

    private final Pipeline pipeline;

    @Override
    public Runnable handle(GracePeriodConfirmedIntegrationEvent event) {
        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Ordering", event );
        var command = new SetAwaitingValidationOrderStatusCommand(event.getOrderId());
        log.info("----- Sending command: {} - {}: {} {}",
            command.getClass().getSimpleName(), "OrderNumber", command.getOrderNumber(), command);
        Runnable task = () -> pipeline.send(command);
        task.run();
        return task;
    }

}
