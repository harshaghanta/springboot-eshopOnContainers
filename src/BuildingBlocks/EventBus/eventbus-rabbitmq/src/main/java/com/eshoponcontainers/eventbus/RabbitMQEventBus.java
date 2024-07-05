package com.eshoponcontainers.eventbus;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.springframework.context.ApplicationContext;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.abstractions.EventBusSubscriptionManager;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;
import com.eshoponcontainers.eventbus.events.IntegrationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RabbitMQEventBus implements EventBus {

    private String subscribtionName;
    private EventBusSubscriptionManager eventBusSubscriptionManager;
    private DefaultRabbitMQPersistentConnection persistentConnection;
    private Channel consumerChannel;
    private ApplicationContext context;
    /**
     *
     */
    private static final String EXCHANGE_DIRECT = "direct";
    private static final String EXCHANGE_NAME = "eshop_event_bus";

    public RabbitMQEventBus(DefaultRabbitMQPersistentConnection persistentConnection,
            EventBusSubscriptionManager eventBusSubscriptionManager,
            ApplicationContext context, String subscribtionName) {

        this.persistentConnection = persistentConnection;
        this.eventBusSubscriptionManager = eventBusSubscriptionManager;
        this.subscribtionName = subscribtionName;
        this.consumerChannel = createConsumerChannel();
        this.context = context;

    }

    @Override
    public void publish(IntegrationEvent event) {

        if (!persistentConnection.isConnected()) {
            persistentConnection.tryConnect();
        }

        String eventName = event.getClass().getSimpleName();
        log.trace("Creating RabbitMQ channel to publish event: {} ({})", event.getId(), eventName);

        Channel channel = persistentConnection.createChannel();

        log.trace("Declaring RabbitMQ exchange to publish event: {}", event.getId());
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_DIRECT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // TODO: HIGH : INJECT OBJECTMAPPER
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        byte[] messageBytes = null;
        try {
            messageBytes = objectMapper.writeValueAsBytes(event);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }

        BasicProperties properties = new AMQP.BasicProperties().builder().deliveryMode(2).build();

        log.trace("Publishing event to RabbitMQ: {}", event.getId());
        try {
            channel.basicPublish(EXCHANGE_NAME, eventName, true, properties, messageBytes);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void subscribe(Class<T> eventType,
            Class<TH> eventHandlerType) {

        String eventName = eventBusSubscriptionManager.getEventKey(eventType);
        doInternalSubscription(eventName);

        log.info("Subscribing to event {} with {}", eventName, eventHandlerType.getName());
        eventBusSubscriptionManager.addSubscription(eventType, eventHandlerType);

        startBasicConsume();
    }

    private void doInternalSubscription(String eventName) {

        boolean hasSubscriptionsForEvent = eventBusSubscriptionManager.hasSubscriptionsForEvent(eventName);
        if (!hasSubscriptionsForEvent) {

            if (!persistentConnection.isConnected()) {
                persistentConnection.tryConnect();
            }

            try {
                consumerChannel.queueBind(subscribtionName, EXCHANGE_NAME, eventName);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private Channel createConsumerChannel() {
        if (!persistentConnection.isConnected()) {
            persistentConnection.tryConnect();
        }

        log.trace("Creating RabbitMQ consumer channel");

        Channel channel = persistentConnection.createChannel();
        try {
            channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_DIRECT);
            channel.queueDeclare(subscribtionName, true, false, false, null);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return channel;
    }

    private void startBasicConsume() {
        log.info("Starting RabbitMQ basic consume");

        if (consumerChannel != null) {

            DefaultConsumer consumer = new DefaultConsumer(consumerChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties,
                        byte[] body) throws IOException {

                    try {

                        // TODO: HIGH : INJECT OBJECT MAPPER

                        ObjectMapper objectMapper = new ObjectMapper();
                        objectMapper.findAndRegisterModules();

                        String eventName = envelope.getRoutingKey();
                        log.debug("Consuming event: {}", eventName);

                        Class eventType = eventBusSubscriptionManager.getEventTypeByName(eventName);
                        Object event = objectMapper.readValue(body, eventType);

                        log.info("Event received: {}", event);

                        List<SubscriptionInfo> subscriptions = eventBusSubscriptionManager
                                .getHandlersForEvent(eventType);

                        for (SubscriptionInfo subscription : subscriptions) {

                            Class handler = subscription.getHandler();
                            try {
                                // Object instance = handler.newInstance();
                                Object instance = context.getBean(handler);
                                Method methodInfo = handler.getMethod("handle", eventType);
                                Runnable runnable = (Runnable) methodInfo.invoke(instance, event);
                                runnable.run();

                            } catch (IllegalAccessException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (SecurityException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (IllegalArgumentException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        consumerChannel.basicAck(envelope.getDeliveryTag(), false);

                    } catch (Exception e) {
                        log.error("Error occured in handleDelivery: {}", e.getMessage());
                    }

                }

            };

            try {
                consumerChannel.basicConsume(subscribtionName, false, consumer);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            log.error("StartBasicConsume can't call on consumerChannel == null");
        }
    }

    @Override
    public <T extends IntegrationEvent, TH extends IntegrationEventHandler<T>> void unsubscribe(Class<T> eventType,
            Class<TH> eventhandlerType) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unsubscribe'");
    }

}