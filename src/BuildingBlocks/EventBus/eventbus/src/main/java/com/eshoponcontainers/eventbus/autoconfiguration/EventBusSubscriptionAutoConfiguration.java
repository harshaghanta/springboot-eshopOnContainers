package com.eshoponcontainers.eventbus.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eshoponcontainers.eventbus.abstractions.EventBusSubscriptionManager;
import com.eshoponcontainers.eventbus.impl.InMemoryEventBusSubscriptionManager;

@Configuration
public class EventBusSubscriptionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventBusSubscriptionManager getEventBusSubscriptionManager() {
        return new InMemoryEventBusSubscriptionManager();
    }
}
