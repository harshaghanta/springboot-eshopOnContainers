package com.eshoponcontainers.orderapi.application.integrationEvents.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)

public class OrderPaymentSucceededIntegrationEvent extends IntegrationEvent {

    private int orderId;
}
