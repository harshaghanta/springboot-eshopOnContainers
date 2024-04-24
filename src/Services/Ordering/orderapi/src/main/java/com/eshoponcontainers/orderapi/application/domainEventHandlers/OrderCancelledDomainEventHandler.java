package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.aggregatesModel.orderAggregate.Order;
import com.eshoponcontainers.aggregatesModel.orderAggregate.OrderStatus;
import com.eshoponcontainers.events.OrderCancelledDomainEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.IOrderingIntegrationEventService;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStatusChangedToCancelledIntegrationEvent;
import com.eshoponcontainers.repositories.BuyerRepository;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderCancelledDomainEventHandler implements Notification.Handler<OrderCancelledDomainEvent>  {

    private final IOrderingIntegrationEventService orderingIntegrationEventService;
    private final BuyerRepository buyerRepository;
    private final IOrderRepository orderRepository;

    @Override
    public void handle(OrderCancelledDomainEvent event) {
        log.trace("Order with Id:{} has been successfully updated to status {}", event.getOrder().getId(), OrderStatus.Cancelled.name());
        
        Order order = orderRepository.get(event.getOrder().getId());        
        var buyer = buyerRepository.findById(order.getBuyerId().toString());

        var orderStatusChangedToCancelledIntegrationEvent = new OrderStatusChangedToCancelledIntegrationEvent(order.getId(), order.getOrderStatus().name(), buyer.getName());
        orderingIntegrationEventService.addAndSaveEvent(orderStatusChangedToCancelledIntegrationEvent);        
    }
}
