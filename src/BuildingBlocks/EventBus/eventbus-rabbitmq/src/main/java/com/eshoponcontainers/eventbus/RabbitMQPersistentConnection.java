package com.eshoponcontainers.eventbus;

import com.rabbitmq.client.Channel;

public interface RabbitMQPersistentConnection {
    
    Channel createChannel();

    Boolean tryConnect();

    Boolean isConnected();
}
