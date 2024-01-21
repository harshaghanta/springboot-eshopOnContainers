package com.eshoponcontainers.orderingbackgroundtasks.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GracePeriodConfirmedIntegrationEvent extends IntegrationEvent {

    private int orderId;
}
