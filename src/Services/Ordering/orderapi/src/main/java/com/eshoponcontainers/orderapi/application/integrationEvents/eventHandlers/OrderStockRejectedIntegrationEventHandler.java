package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.SetStockConfirmedOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.commands.SetStockRejectedOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockRejectedIntegrationEvent;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderStockRejectedIntegrationEventHandler implements IntegrationEventHandler<OrderStockRejectedIntegrationEvent> {

    private final Pipeline pipeline;

    @Override
    public Runnable handle(OrderStockRejectedIntegrationEvent event) {
        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Ordering", event );
        
        var orderStockRejectedItems = event.getOrderStockItems().stream()
            .filter(x -> !x.isHasStock())
            .map(x-> x.getProductId())
            .collect(Collectors.toList());

        var command = new SetStockRejectedOrderStatusCommand(event.getOrderId(), orderStockRejectedItems);
        log.info("----- Sending command: {} - {}: {} {}",
            command.getClass().getSimpleName(), "OrderNumber", command.getOrderNumber(), command);
        Runnable task = () -> pipeline.send(command);
        task.run();
        return task;
    }

}
