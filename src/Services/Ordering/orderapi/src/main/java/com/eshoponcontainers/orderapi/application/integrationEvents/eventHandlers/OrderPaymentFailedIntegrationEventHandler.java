package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.entities.InboxMessage;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.CancelOrderCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderPaymentFailedIntegrationEvent;
import com.eshoponcontainers.repositories.InboxRepository;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderPaymentFailedIntegrationEventHandler implements IntegrationEventHandler<OrderPaymentFailedIntegrationEvent> {

    private final Pipeline pipeline;
    private final InboxRepository inboxRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OrderPaymentFailedIntegrationEvent event) {
        
        if(inboxRepository.existsById(event.getId()))
        {
            log.info("Event with id {} already processed. Skipping.", event.getId());
            return;
        }

        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Ordering", event );
        var command = new CancelOrderCommand(event.getOrderId());
        log.info("----- Sending command: {} - {}: {} {}",
            command.getClass().getSimpleName(), "OrderNumber", command.getOrderNumber(), command);
        pipeline.send(command);        
        inboxRepository.save(new InboxMessage(event.getId(), event.getClass().getName()));
    }

}
