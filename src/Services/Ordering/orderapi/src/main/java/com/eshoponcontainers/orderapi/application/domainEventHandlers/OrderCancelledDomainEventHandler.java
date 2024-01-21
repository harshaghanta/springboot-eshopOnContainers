package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.events.OrderCancelledDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.IOrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToCancelledIntegrationEvent;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCancelledDomainEventHandler implements Notification.Handler<OrderCancelledDomainEvent>  {

    private final IOrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    public void handle(OrderCancelledDomainEvent event) {
        Order order = event.getOrder();
        log.info("Order {} cancelled", order.getId());
        var orderStatusChangedToCancelledIntegrationEvent = new OrderStatusChangedToCancelledIntegrationEvent(order.getId(), order.getOrderStatus().name(), order.getBuyer().getName());
        UUID transactionId = UUID.randomUUID();
        orderingIntegrationEventService.addAndSaveEvent(orderStatusChangedToCancelledIntegrationEvent, transactionId);
        orderingIntegrationEventService.publishEventsThroughEventBus(transactionId);
    }
}
