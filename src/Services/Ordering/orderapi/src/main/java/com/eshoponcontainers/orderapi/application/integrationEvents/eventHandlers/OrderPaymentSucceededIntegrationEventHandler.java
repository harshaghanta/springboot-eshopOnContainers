package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.SetPaidOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderPaymentSucceededIntegrationEvent;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderPaymentSucceededIntegrationEventHandler implements IntegrationEventHandler<OrderPaymentSucceededIntegrationEvent> {

    private final Pipeline pipeline;

    @Override
    public Runnable handle(OrderPaymentSucceededIntegrationEvent event) {
        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Ordering", event );
        var command = new SetPaidOrderStatusCommand(event.getOrderId());
        log.info("----- Sending command: {} - {}: {} {}",
            command.getClass().getSimpleName(), "OrderNumber", command.getOrderNumber(), command);
        Runnable task = () -> pipeline.send(command);
        task.run();
        return task;
    }

}
