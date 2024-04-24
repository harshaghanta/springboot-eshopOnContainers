package com.eshoponcontainers.paymentapi.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class OrderPaymentSucceededIntegrationEvent extends IntegrationEvent {
    private int orderId;
}
