package com.eshoponcontainers.eventbus;

import com.rabbitmq.client.Channel;

public interface RabbitMQPersistentConnection {
    
    Channel createChannel();

    boolean tryConnect();

    boolean isConnected();
}
