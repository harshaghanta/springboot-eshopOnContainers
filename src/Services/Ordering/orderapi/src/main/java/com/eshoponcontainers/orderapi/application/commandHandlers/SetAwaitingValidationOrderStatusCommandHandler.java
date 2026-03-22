package com.eshoponcontainers.orderapi.application.commandHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.orderapi.application.commands.SetAwaitingValidationOrderStatusCommand;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.services.TransactionContext;

import an.awesome.pipelinr.Command;
import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor

@Slf4j
public class SetAwaitingValidationOrderStatusCommandHandler
        implements Command.Handler<SetAwaitingValidationOrderStatusCommand, Boolean> {

    private final IOrderRepository orderRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;
    private final Pipeline pipeline;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean handle(SetAwaitingValidationOrderStatusCommand command) {

        log.info("Executing SetAwaitingValidationOrderStatusCommand & Fetching Order: {}", command.getOrderNumber());

        TransactionContext.beginTransactionContext();
        var transactionId = TransactionContext.getTransactionId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.info("Transaction completed with COMMIT status.");
                    // orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
                    var domainEvents = DomainContext.getDomainEvents();
                    DomainContext.clearContext(); // Safety: clear after sending
                    if (domainEvents != null) {
                        domainEvents.forEach(event -> {
                            log.info("Publishing domain event: {}", event.getClass().getSimpleName());
                            pipeline.send(event);
                        });

                    }
                } else if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                    log.error("Transaction completed with ROLLBACK status.");
                } else {
                    log.info("Transaction completed with unknown status: {}", status);
                }
                TransactionContext.clearContext();
            }
        });

        Order order = orderRepository.get(command.getOrderNumber());
        if (order == null) {
            log.warn("Order not found: {}", command.getOrderNumber());
            return false;
        }
        order.setAwaitingValidationStatus();
        log.info("Order status set to Awaiting Validation: {}", command.getOrderNumber());
        // return orderRepository.getUnitOfWork().saveChanges();
        return true;
    }

}
