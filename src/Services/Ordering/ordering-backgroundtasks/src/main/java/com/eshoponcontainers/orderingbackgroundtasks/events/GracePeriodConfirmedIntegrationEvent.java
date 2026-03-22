package com.eshoponcontainers.orderingbackgroundtasks.events;

import com.eshoponcontainers.eventbus.events.IntegrationEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GracePeriodConfirmedIntegrationEvent extends IntegrationEvent {

    private int orderId;
}
