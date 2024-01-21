package com.eshoponcontainers.orderingbackgroundtasks;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

import com.eshoponcontainers.orderingbackgroundtasks.repositories.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class OrderingBackgroundtasksApplication {

	@Autowired
	private OrderRepository orderRepository;


	private volatile boolean shutdownRequested = false;

	public static void main(String[] args) {

		ConfigurableApplicationContext context = SpringApplication.run(OrderingBackgroundtasksApplication.class, args);
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.warn("Received shutdown signal...");
			context.getBean(OrderingBackgroundtasksApplication.class).handleShutdown();
		}));
	}

	@Scheduled(fixedDelay = 10000) // Run every 1000 milliseconds (1 second)
	public void backgroundProcess() {
		if (shutdownRequested) {

		} else {
			List<Integer> orders = orderRepository.getConfirmedGracePeriodOrders();
			for (Integer orderId : orders) {
				// GracePeriodConfirmedIntegrationEvent event = new GracePeriodConfirmedIntegrationEvent(orderId);
				// eventBus.publish(event);
			}
		}
		log.info("GracePeriodManagerService started");

	}

	// Method to handle shutdown and pause ongoing work
	public synchronized void handleShutdown() {

	}

}
