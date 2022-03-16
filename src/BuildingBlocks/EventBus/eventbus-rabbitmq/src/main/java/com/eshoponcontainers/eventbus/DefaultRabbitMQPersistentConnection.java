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
        } catch (IOException e) {
            throw illegalStateException;            
        }
        return channel;
    }

    @Override
    public Boolean tryConnect() {
        try {
            connection = connectionFactory.newConnection();
            if(isConnected()) {
                //TODO : HIGH NEED TO ADD EVENT HANDLERS
                return true;
            }
            else {
                return false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;


    }

    @Override
    public Boolean isConnected() {
        //TODO: HIGH DISPOSE IMPLEMENTATION PENDING
        return connection != null && connection.isOpen();
    }

}
