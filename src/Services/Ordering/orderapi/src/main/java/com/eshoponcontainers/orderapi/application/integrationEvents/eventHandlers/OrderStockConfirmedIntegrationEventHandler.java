package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.entities.InboxMessage;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.SetStockConfirmedOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockConfirmedIntegrationEvent;
import com.eshoponcontainers.repositories.InboxRepository;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStockConfirmedIntegrationEventHandler implements IntegrationEventHandler<OrderStockConfirmedIntegrationEvent> {

    private final Pipeline pipeline;
    private final InboxRepository inboxRepository;
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OrderStockConfirmedIntegrationEvent event) {
        if(inboxRepository.existsById(event.getId()))
        {
            log.info("Event with id {} already processed. Skipping.", event.getId());
            return;
        }

        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Ordering", event );
        var command = new SetStockConfirmedOrderStatusCommand(event.getOrderId());
        log.info("----- Sending command: {} - {}: {} {}",
            command.getClass().getSimpleName(), "OrderNumber", command.getOrderNumber(), command);
        pipeline.send(command);
        inboxRepository.save(new InboxMessage(event.getId(), event.getClass().getName()));
    }

}
