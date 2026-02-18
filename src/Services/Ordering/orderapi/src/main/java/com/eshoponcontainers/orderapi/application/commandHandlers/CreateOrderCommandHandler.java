package com.eshoponcontainers.orderapi.application.commandHandlers;

import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Address;
import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.orderapi.application.commands.CreateOrderCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStartedIntegrationEvent;
import com.eshoponcontainers.orderapi.application.viewModels.OrderItemDTO;
import com.eshoponcontainers.orderapi.services.TransactionContext;

import org.springframework.transaction.support.TransactionSynchronization;


import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateOrderCommandHandler implements Command.Handler<CreateOrderCommand, Boolean> {

    private final OrderingIntegrationEventService orderingIntegrationEventService;
    private final IOrderRepository orderRepository;
    private final Pipeline pipeline;

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Boolean handle(CreateOrderCommand command) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCommit() {
                        log.info("Transaction has been committed.");
                        var domainEvents = DomainContext.getDomainEvents();
                        if (domainEvents != null) {
                            log.info("Domain events count: {}", domainEvents.size());
                            domainEvents.forEach(e -> pipeline.send(e));
                        } else {
                            log.info("No domain events found.");
                        }

                    }                    
                });
                
        log.info("Received CreateOrderCommand: {}", command);
        var orderStartedIntegrationEvent = new OrderStartedIntegrationEvent(command.getUserId());
        orderingIntegrationEventService.addAndSaveEvent(orderStartedIntegrationEvent);

        var address = new Address(command.getStreet(), command.getCity(), command.getState(), command.getCountry(),
                command.getZipCode());
        var order = new Order(command.getUserId(), command.getUserName(), address, command.getCardTypeId(),
                command.getCardNumber(), command.getCardSecurityNumber(), command.getCardHolderName(),
                command.getCardExpiration(), null, null);

        for (OrderItemDTO orderItem : command.getOrderItems()) {
            order.addOrderItem(orderItem.productId(), orderItem.productName(), orderItem.unitPrice()   , orderItem.discount(), orderItem.pictureUrl(), orderItem.units());
        }

        log.info("Creating Order: - {}", order);
        orderRepository.add(order);
        return true;
        // return orderRepository.getUnitOfWork().saveChanges();
    }

}
