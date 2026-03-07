package com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.entities.InboxMessage;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.UserCheckoutAcceptedIntegrationEvent;
import com.eshoponcontainers.repositories.InboxRepository;

import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCheckOutAcceptedIntegrationEventHandler
        implements IntegrationEventHandler<UserCheckoutAcceptedIntegrationEvent> {

    private final Pipeline pipeline;
    private final InboxRepository inboxRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(UserCheckoutAcceptedIntegrationEvent event) {

        if (inboxRepository.existsById(event.getId())) {
            log.info("Event with id {} already processed. Skipping.", event.getId());
            return;
        }

        log.info("----- UserCheckoutAcceptedIntegrationEvent Received: {}----", event);

        if (event.getRequestId() != null) {

            var command = new CreateOrderCommand(event.getBasket().getItems().stream().collect(Collectors.toList()),
                    event.getUserId(), event.getUserName(), event.getCity(),
                    event.getStreet(),
                    event.getState(), event.getCountry(), event.getZipCode(), event.getCardNumber(),
                    event.getCardHolderName(), event.getCardExpiration(),
                    event.getCardSecurityNumber(), event.getCardTypeId());

            Boolean orderCreated = pipeline.send(command);
            if (orderCreated) {
                log.info("----- CreateOrderCommand suceeded - RequestId: {}", event.getRequestId());
            } else {
                log.warn("CreateOrderCommand failed - RequestId: {}", event.getRequestId());
            }
        } else {
            log.warn("Invalid IntegrationEvent - RequestId is missing - {}", event);
        }
        inboxRepository.save(new InboxMessage(event.getId(), event.getClass().getName()));
    }

}
