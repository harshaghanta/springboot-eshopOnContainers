package com.eshoponcontainers.eventbus;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class DefaultRabbitMQPersistentConnection implements RabbitMQPersistentConnection
{
    
    private ConnectionFactory connectionFactory;
    private int retryCount;
    private Connection connection;

    public DefaultRabbitMQPersistentConnection(ConnectionFactory connectionFactory, int retryCount) {
        this.connectionFactory = connectionFactory;
        this.retryCount = retryCount;
    }

    @Override
    public Channel createChannel() {
        Channel channel = null;
        IllegalStateException illegalStateException = new IllegalStateException("No RabbitMQ connections are available to perform this action");

        if(!isConnected()) {
            throw illegalStateException;
        }

         try {
            channel = connection.createChannel();
        } catch (IOException _) {
            throw illegalStateException;            
        }
        return channel;
    }

    @Override
    public boolean tryConnect() {
        try {
            connection = connectionFactory.newConnection();
            return isConnected();
            
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        } 
        return false;
    }

    @Override
    public boolean isConnected() {
        //TODO: HIGH DISPOSE IMPLEMENTATION PENDING
        return connection != null && connection.isOpen();
    }

}
