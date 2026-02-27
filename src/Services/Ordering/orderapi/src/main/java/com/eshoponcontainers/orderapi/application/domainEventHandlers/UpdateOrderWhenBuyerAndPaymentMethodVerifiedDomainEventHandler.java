package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import com.eshoponcontainers.aggregatesModel.orderAggregate.IOrderRepository;
import com.eshoponcontainers.events.BuyerAndPaymentMethodVerifiedDomainEvent;
import com.eshoponcontainers.orderapi.aop.MyTransactional;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateOrderWhenBuyerAndPaymentMethodVerifiedDomainEventHandler implements Notification.Handler<BuyerAndPaymentMethodVerifiedDomainEvent> {

    private final IOrderRepository orderRepository;

    @Override
    @MyTransactional(propagation = Propagation.REQUIRES_NEW)
    public void handle(BuyerAndPaymentMethodVerifiedDomainEvent event) {
        log.info("Receieved the BuyerAndPaymentMethodVerifiedDomainEvent with buyer: {} for order: {}" , event.getBuyer().getId(),
            event.getOrderId() );
        var order = orderRepository.get(event.getOrderId());
        order.setBuyerId(event.getBuyer().getId());
        order.setPaymentMethodId(event.getPaymentMethod().getId());
        // orderRepository.getUnitOfWork().saveChanges();

        log.info("Order with Id: {} has been successfully updated with a payment method {} {}", event.getOrderId(), event.getPaymentMethod().getId());
    }

}
