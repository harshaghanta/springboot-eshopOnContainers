package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.entities.InboxMessage;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.SetStockRejectedOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockRejectedIntegrationEvent;
import com.eshoponcontainers.repositories.InboxRepository;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OrderStockRejectedIntegrationEventHandler
        implements IntegrationEventHandler<OrderStockRejectedIntegrationEvent> {

    private final Pipeline pipeline;
    private final InboxRepository inboxRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OrderStockRejectedIntegrationEvent event) {
        if (inboxRepository.existsById(event.getId())) {
            log.info("Event with id {} already processed. Skipping.", event.getId());
            return;
        }

        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Ordering", event);

        var orderStockRejectedItems = event.getOrderStockItems().stream()
                .filter(x -> !x.isHasStock())
                .map(x -> x.getProductId())
                .collect(Collectors.toList());

        var command = new SetStockRejectedOrderStatusCommand(event.getOrderId(), orderStockRejectedItems);
        log.info("----- Sending command: {} - {}: {} {}",
                command.getClass().getSimpleName(), "OrderNumber", command.getOrderNumber(), command);
        pipeline.send(command);
        inboxRepository.save(new InboxMessage(event.getId(), event.getClass().getName()));
    }

}
