package com.eshoponcontainers.eventbus.autoconfiguration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eshoponcontainers.eventbus.DefaultRabbitMQPersistentConnection;
import com.eshoponcontainers.eventbus.RabbitMQEventBus;
import com.eshoponcontainers.eventbus.RabbitMQPersistentConnection;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.abstractions.EventBusSubscriptionManager;
import com.rabbitmq.client.ConnectionFactory;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "eventbus")
@Data
public class RabbitMQEventBusAutoConfiguration {

    private String host;
    private String username;
    private String password;
    private String subscriptionClientName;

    @Bean
    @ConditionalOnMissingBean(type = "com.rabbitmq.client.ConnectionFactory")
    public ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        log.info("--------------PRINTING EVENTUBUS DETAILS HOST: {}--------------------", host);
        factory.setHost(host);
        if (!username.isBlank() && username != null)
            factory.setUsername(username);

        if (!password.isBlank() && password != null)
            factory.setPassword(password);

        return factory;
    }

    @Bean
    @ConditionalOnMissingBean(type = "com.eshoponcontainers.eventbus.RabbitMQPersistentConnection")
    public RabbitMQPersistentConnection getRabbitMQPersistentConnection(ConnectionFactory connectionFactory) {
        DefaultRabbitMQPersistentConnection persistentConnection = new DefaultRabbitMQPersistentConnection(
                connectionFactory, 5);
        return persistentConnection;
    }    

    @Bean
    @ConditionalOnMissingBean(type = "com.eshoponcontainers.eventbus.abstractions.EventBus")
    public EventBus getEventBus(RabbitMQPersistentConnection persistentConnection,
            EventBusSubscriptionManager eventBusSubscriptionManager, ApplicationContext context) {
        return new RabbitMQEventBus((DefaultRabbitMQPersistentConnection) persistentConnection,
                eventBusSubscriptionManager, context, subscriptionClientName);
    }

}
