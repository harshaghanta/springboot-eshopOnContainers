package com.eshoponcontainers.paymentapi.integrationEvents.eventHandlers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.paymentapi.integrationEvents.events.OrderPaymentSucceededIntegrationEvent;
import com.eshoponcontainers.paymentapi.integrationEvents.events.OrderStatusChangedToStockConfirmedIntegrationEvent;
import com.eshoponcontainers.services.impl.OutboxService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusChangedToStockConfirmedIntegrationEventHandler
        implements IntegrationEventHandler<OrderStatusChangedToStockConfirmedIntegrationEvent> {

    // private final EventBus eventBus;
    private final ObjectMapper objectMapper;
    private final OutboxService outboxService;

    @Override
    @Transactional
    public void handle(OrderStatusChangedToStockConfirmedIntegrationEvent event) {

        log.info("----- Handling integration event: {} at {} - {}", event.getId(), "Payment", event);
        log.info("OrderStatusChangedToStockConfirmedIntegrationEvent received: OrderNumber:{}", event.getOrderId());
        var paymentEvent = new OrderPaymentSucceededIntegrationEvent(event.getOrderId());
        log.info("PaymentEvent created: for OrderNumber:{}", paymentEvent.getOrderId());
        // TODO: HIGH: CHECK HOW TO IMPLEMENT THE SAME LOG LINE AS IN THE ORIGINAL CODE
        log.info("----- Publishing integration event: {} from {} - {}", paymentEvent.getId(), "Payment",
                paymentEvent);
        try {
            log.info("Printing the paymentEvent: {}", objectMapper.writeValueAsString(paymentEvent));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        outboxService.saveToOutbox(paymentEvent);
        // eventBus.publish(paymentEvent);

    }

}
