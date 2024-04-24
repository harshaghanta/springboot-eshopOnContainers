package com.eshoponcontainers.paymentapi.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OrderPaymentFailedIntegrationEvent extends IntegrationEvent  {
    private int orderId;
}
