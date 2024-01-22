package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.events.OrderShippedDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.OrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToShippedIntegrationEvent;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderShippedDomainEventHandler implements Notification.Handler<OrderShippedDomainEvent> {

    private final IOrderRepository orderRepository;
    private final OrderingIntegrationEventService orderingIntegrationEventService;

    @Override
    public void handle(OrderShippedDomainEvent event) {

        log.trace("Order with Id: {} has been successfully updated to status {} ({})",
                event.getOrder().getId(), OrderStatus.Shipped.name(), OrderStatus.Shipped.getId());

        UUID transactionId = UUID.randomUUID();
        Order order = orderRepository.get(event.getOrder().getId());

        String buyerName = order.getBuyer().getName();
        OrderStatusChangedToShippedIntegrationEvent integrationEvent = new OrderStatusChangedToShippedIntegrationEvent(order.getId(), order.getOrderStatus().name(), buyerName);
        orderingIntegrationEventService.addAndSaveEvent(integrationEvent, transactionId);
    }

}
