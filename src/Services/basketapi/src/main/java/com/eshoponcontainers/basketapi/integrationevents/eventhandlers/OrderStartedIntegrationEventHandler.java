package com.eshoponcontainers.basketapi.integrationevents.eventhandlers;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.basketapi.integrationevents.events.OrderStartedIntegrationEvent;
import com.eshoponcontainers.basketapi.repositories.RedisBasketDataRepository;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderStartedIntegrationEventHandler implements IntegrationEventHandler<OrderStartedIntegrationEvent> {

    private final RedisBasketDataRepository basketDataRepository;

    @Override
    public Runnable handle(OrderStartedIntegrationEvent event) {
        Runnable runnable = new Runnable() 
        {
            @Override
            public void run()
            {
                log.info("Handling integration event: {} at {}  - ({})", event.getId(), "BasketAPI", event);
                basketDataRepository.deleteBasket(event.getUserId());
            }
        };
        
        return runnable;
        
    }
}
