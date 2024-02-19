package com.eshoponcontainers.orderapi.application.domainEventHandlers;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.aggregatesModel.buyerAggregate.Buyer;
import com.eshoponcontainers.aggregatesModel.buyerAggregate.IBuyerRepository;
import com.eshoponcontainers.events.OrderStartedDomainEvent;

import an.awesome.pipelinr.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler
        implements Notification.Handler<OrderStartedDomainEvent> {

    private final IBuyerRepository buyerRepository;

    @Override
    public void handle(OrderStartedDomainEvent event) {

        log.info("ValidateOrAddBuyerAggregateWhenOrderStartedDomainEventHandler invoked");

        var cardTypeId = event.getCardTypeId() != 0 ? event.getCardTypeId() : 1;
        Buyer buyer = buyerRepository.find(event.getUserId());
        boolean buyerExisted = !(buyer == null);

        if (!buyerExisted) {
            buyer = new Buyer(event.getUserId(), event.getUserName());
        }

        String paymentMethodAlias = "Payment Method on " + new Date().toString();

        buyer.verifyOrAddPaymentMethod(cardTypeId, paymentMethodAlias, event.getCardNumber(),
                event.getCardSecurityNumber(), event.getCardHolderName(), event.getCardExpiration(),
                event.getOrder().getId());

        if (buyerExisted)
            buyerRepository.update(buyer);
        else
            buyerRepository.add(buyer);

        buyerRepository.getUnitOfWork().saveChanges();
    }

}
