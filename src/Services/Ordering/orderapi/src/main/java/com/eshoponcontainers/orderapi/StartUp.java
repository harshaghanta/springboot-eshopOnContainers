package com.eshoponcontainers.orderapi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.config.EntityManagerUtil;
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
import com.eshoponcontainers.orderapi.config.OrderingDBConfig;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class StartUp {

    private final EventBus eventBus;
    private final OrderingDBConfig orderingDBConfig;

    @PostConstruct
    public void init() {

        log.info("Initializing EntityManagerFactory for OrderAPI");
        Map<String, String> properties = new HashMap<>();
        properties.put("javax.persistence.jdbc.url", orderingDBConfig.getUrl());
        properties.put("javax.persistence.jdbc.user", orderingDBConfig.getUsername());
        properties.put("javax.persistence.jdbc.password", orderingDBConfig.getPassword());

        // EntityManagerUtil.intializeEntityManagerFactory(properties);

        log.info("Subscribing to the Integration Events in OrderAPI");
        

        eventBus.subscribe(UserCheckoutAcceptedIntegrationEvent.class, UserCheckOutAcceptedIntegrationEventHandler.class);
        eventBus.subscribe(GracePeriodConfirmedIntegrationEvent.class, GracePeriodConfirmedIntegrationEventHandler.class);
        eventBus.subscribe(OrderStockConfirmedIntegrationEvent.class, OrderStockConfirmedIntegrationEventHandler.class);
        eventBus.subscribe(OrderStockRejectedIntegrationEvent.class, OrderStockRejectedIntegrationEventHandler.class);
        eventBus.subscribe(OrderPaymentFailedIntegrationEvent.class, OrderPaymentFailedIntegrationEventHandler.class);
        eventBus.subscribe(OrderPaymentSucceededIntegrationEvent.class, OrderPaymentSucceededIntegrationEventHandler.class);

    }
}
