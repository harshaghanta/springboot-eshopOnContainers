package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import java.text.MessageFormat;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.CancelOrderCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderPaymentFailedIntegrationEvent;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderPaymentFailedIntegrationEventHandler implements IntegrationEventHandler<OrderPaymentFailedIntegrationEvent> {

    private final Pipeline pipeline;

    @Override
    public Runnable handle(OrderPaymentFailedIntegrationEvent event) {
        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Ordering", event );
        var command = new CancelOrderCommand(event.getOrderId());
        log.info("----- Sending command: {} - {}: {} {}",
            command.getClass().getSimpleName(), "OrderNumber", command.getOrderNumber(), command);
        Runnable task = () -> pipeline.send(command);
        task.run();
        return task;
    }

}
