package com.eshoponcontainers.orderingbackgroundtasks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.eshoponcontainers.orderingbackgroundtasks.events.GracePeriodConfirmedIntegrationEvent;
import com.eshoponcontainers.orderingbackgroundtasks.repositories.OrderRepository;
import com.eshoponcontainers.services.impl.OutboxService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BackgroundTaskRunner {

    @Autowired
    private OutboxService outboxService;

    @Autowired
    private OrderRepository orderRepository;

    private volatile boolean shutdownRequested = false;
    private List<Integer> ordersToProcess = new ArrayList<>();

    @Scheduled(fixedDelay = 30000) // Run every 30 seconds
    @Transactional
    public void backgroundProcess() {
        if (shutdownRequested) {
            // No-op
        } else {
            log.info("Fetching confirmed grace period orders..");
            ordersToProcess = orderRepository.getConfirmedGracePeriodOrders();
            log.info("Retrieved {} confirmed grace period orders.", ordersToProcess.size());
            for (Integer orderId : ordersToProcess) {
                GracePeriodConfirmedIntegrationEvent event = new GracePeriodConfirmedIntegrationEvent(orderId);
                log.info("----- Publishing integration event: {} from {} - {}", event.getId(), "Ordering-Backgroundtasks", event);
                // eventBus.publish(event);
                outboxService.saveToOutbox(event);
            }
        }
    }

    // Method to handle shutdown and pause ongoing work
    public synchronized void handleShutdown() {
        while (!ordersToProcess.isEmpty()) {
            log.info("Orders getting processed. Waiting for them to be processed...");
            sleep(Duration.ofSeconds(3));
        }
        shutdownRequested = true;
        log.warn("No orders to process. Shutting down....");
    }

    private void sleep(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
