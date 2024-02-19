package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderPaymentSucceededIntegrationEvent extends IntegrationEvent {

    private int orderId;
}
