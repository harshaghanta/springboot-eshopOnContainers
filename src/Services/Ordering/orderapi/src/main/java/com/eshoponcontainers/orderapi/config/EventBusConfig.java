package com.eshoponcontainers.orderapi.config;


import com.eshoponcontainers.eventbus.DefaultRabbitMQPersistentConnection;
import com.eshoponcontainers.eventbus.RabbitMQEventBus;
import com.eshoponcontainers.eventbus.RabbitMQPersistentConnection;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.abstractions.EventBusSubscriptionManager;
import com.eshoponcontainers.eventbus.impl.InMemoryEventBusSubscriptionManager;
import com.rabbitmq.client.ConnectionFactory;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "eventbus")
@Data
public class EventBusConfig {

    private String host;
    private String username;
    private String password;

    @Bean
    public ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
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
