package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.context.DomainContext;
import com.eshoponcontainers.events.OrderShippedDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToShippedIntegrationEvent;
import com.eshoponcontainers.orderapi.services.TransactionContext;
import com.eshoponcontainers.repositories.BuyerRepository;

import an.awesome.pipelinr.Notification;
import an.awesome.pipelinr.Pipeline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderShippedDomainEventHandler implements Notification.Handler<OrderShippedDomainEvent> {

    private final IOrderRepository orderRepository;
    private final BuyerRepository buyerRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;
    private final Pipeline pipeline;
    
    @Override
    // @MyTransactional(propagation = Propagation.REQUIRES_NEW)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(OrderShippedDomainEvent event) {

        log.trace("Order with Id: {} has been successfully updated to status {} ({})",
                event.getOrder().getId(), OrderStatus.Shipped.name(), OrderStatus.Shipped.getId());

        TransactionContext.beginTransactionContext();
        var transactionId = TransactionContext.getTransactionId();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {            

            @Override
            public void afterCompletion(int status) {
                if (status == TransactionSynchronization.STATUS_COMMITTED) {
                    log.info("Transaction completed with COMMIT status.");
                    orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
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

        Order order = orderRepository.get(event.getOrder().getId());
        var buyer = buyerRepository.findById(order.getBuyerId().toString());
        
        OrderStatusChangedToShippedIntegrationEvent integrationEvent = new OrderStatusChangedToShippedIntegrationEvent(order.getId(), order.getOrderStatus().name(), buyer.getName());
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent);
    }

}
