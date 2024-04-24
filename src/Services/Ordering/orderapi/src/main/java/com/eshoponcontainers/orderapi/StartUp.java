package com.eshoponcontainers.orderapi;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers.GracePeriodConfirmedIntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers.OrderPaymentFailedIntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers.OrderPaymentSucceededIntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers.OrderStockConfirmedIntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers.OrderStockRejectedIntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.integrationEvents.eventHandlers.UserCheckOutAcceptedIntegrationEventHandler;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.GracePeriodConfirmedIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderPaymentFailedIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderPaymentSucceededIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockConfirmedIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.OrderStockRejectedIntegrationEvent;
import com.eshoponcontainers.orderapi.application.integrationEvents.events.UserCheckoutAcceptedIntegrationEvent;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartUp {

    private final EventBus eventBus;

    @PostConstruct
    public void init() {
        log.info("Subscribing to the Integration Events in OrderAPI");

        eventBus.subscribe(UserCheckoutAcceptedIntegrationEvent.class, UserCheckOutAcceptedIntegrationEventHandler.class);
        eventBus.subscribe(GracePeriodConfirmedIntegrationEvent.class, GracePeriodConfirmedIntegrationEventHandler.class);
        eventBus.subscribe(OrderStockConfirmedIntegrationEvent.class, OrderStockConfirmedIntegrationEventHandler.class);
        eventBus.subscribe(OrderStockRejectedIntegrationEvent.class, OrderStockRejectedIntegrationEventHandler.class);
        eventBus.subscribe(OrderPaymentFailedIntegrationEvent.class, OrderPaymentFailedIntegrationEventHandler.class);
        eventBus.subscribe(OrderPaymentSucceededIntegrationEvent.class, OrderPaymentSucceededIntegrationEventHandler.class);

    }
}
