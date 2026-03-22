package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
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
import com.eshoponcontainers.services.impl.OutboxService;

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
    private final OutboxService outboxService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    // @MyTransactional
    public Boolean handle(CreateOrderCommand command) {
        TransactionContext.beginTransactionContext();
        var transactionId = TransactionContext.getTransactionId();
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {

                    @Override
                    public void afterCommit() {
                        log.info("Transaction has been committed.");
                            //  orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
                        var domainEvents = DomainContext.getDomainEvents();
                        DomainContext.clearContext(); // Clear after fetching
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
        outboxService.saveToOutbox(orderStartedIntegrationEvent);
        // orderingIntegrationEventService.addAndSaveEvent(orderStartedIntegrationEvent);



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

        TransactionContext.clearContext();
        return true;
        // return orderRepository.getUnitOfWork().saveChanges();
    }

}
