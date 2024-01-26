package com.eshoponcontainers.catalogapi.config;

import com.eshoponcontainers.eventbus.DefaultRabbitMQPersistentConnection;
import com.eshoponcontainers.eventbus.RabbitMQEventBus;
import com.eshoponcontainers.eventbus.RabbitMQPersistentConnection;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.abstractions.EventBusSubscriptionManager;
import com.eshoponcontainers.eventbus.impl.InMemoryEventBusSubscriptionManager;
import com.rabbitmq.client.ConnectionFactory;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class EventBusConfig {

    @Value("${eventbus.host}")
    private String eventBusHost;

    @Value("${eventbus.username}")
    private String eventBusUserName;

    @Value("${eventbus.password}")
    private String eventBusPassword;


    @Bean
    public ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        log.info("--------------PRINTING EVENTUBUS DETAILS HOST: {}--------------------", eventBusHost);
        factory.setHost(eventBusHost);
        if(!eventBusUserName.isBlank() && eventBusUserName != null)
            factory.setUsername(eventBusUserName);

        if(!eventBusPassword.isBlank() && eventBusPassword != null)
            factory.setPassword(eventBusPassword);
        
        return factory;
    }

    @Bean
    public RabbitMQPersistentConnection getRabbitMQPersistentConnection(ConnectionFactory connectionFactory) {
         DefaultRabbitMQPersistentConnection persistentConnection = new DefaultRabbitMQPersistentConnection(connectionFactory , 5);         
         return persistentConnection;
    }

    @Bean
    public EventBusSubscriptionManager getEventBusSubscriptionManager() {
        return new InMemoryEventBusSubscriptionManager();
    }

    @Bean
    public EventBus getEventBus(RabbitMQPersistentConnection persistentConnection,
    EventBusSubscriptionManager eventBusSubscriptionManager) {
        return new RabbitMQEventBus((DefaultRabbitMQPersistentConnection) persistentConnection, eventBusSubscriptionManager, "Catalog");
    }
}
