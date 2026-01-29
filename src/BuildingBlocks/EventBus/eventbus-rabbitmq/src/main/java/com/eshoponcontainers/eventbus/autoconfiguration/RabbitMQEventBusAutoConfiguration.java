package com.eshoponcontainers.eventbus.autoconfiguration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

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
@Data
public class RabbitMQEventBusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(type = "com.rabbitmq.client.ConnectionFactory")
    public ConnectionFactory getConnectionFactory(Environment env) {
        String secretsPath = env.getProperty("SECRETS_PATH", "/vault/secrets");
        String username = readSecret(secretsPath + "/EVENTBUS_USER_NAME");
        String password = readSecret(secretsPath + "/EVENTBUS_PASSWORD");
        ConnectionFactory factory = new ConnectionFactory();
        String host = env.getProperty("EVENTBUS_HOST");
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
            EventBusSubscriptionManager eventBusSubscriptionManager, Environment env, ApplicationContext context) {
        return new RabbitMQEventBus((DefaultRabbitMQPersistentConnection) persistentConnection,
                eventBusSubscriptionManager, context, env.getProperty("EVENTBUS_SUBSCRIPTION_CLIENT_NAME"));
    }

    private String readSecret(String filePath) {
        try {
            // .trim() is crucial because K8s secret files often have a trailing newline
            return Files.readString(Paths.get(filePath)).trim();
        } catch (IOException e) {
            throw new RuntimeException("Could not read database secret at " + filePath, e);
        }
    }

}
