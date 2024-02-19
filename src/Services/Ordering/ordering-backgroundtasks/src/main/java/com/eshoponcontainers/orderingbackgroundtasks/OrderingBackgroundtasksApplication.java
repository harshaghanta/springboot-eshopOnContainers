package com.eshoponcontainers.orderingbackgroundtasks;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.orderingbackgroundtasks.events.GracePeriodConfirmedIntegrationEvent;
import com.eshoponcontainers.orderingbackgroundtasks.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class OrderingBackgroundtasksApplication {

	private final EventBus eventBus;
	private final OrderRepository orderRepository;

	private volatile boolean shutdownRequested = false;
	private List<Integer> ordersToProcess = new ArrayList<>();

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
			log.info("Fetching confirmed grace period orders..");
			ordersToProcess = orderRepository.getConfirmedGracePeriodOrders();
			log.info("Retrieved {} confirmed grace period orders.", ordersToProcess.size());
			for (Integer orderId : ordersToProcess) {
				GracePeriodConfirmedIntegrationEvent event = new GracePeriodConfirmedIntegrationEvent(orderId);
				log.info("----- Publishing integration event: {} from {} - {}", event.getId(), "Ordering-Backgroundtasks", event);
				eventBus.publish(event);
			}
		}
	}

	// Method to handle shutdown and pause ongoing work
	public synchronized void handleShutdown() {

		while (!ordersToProcess.isEmpty()) {
			log.info("Orders getting processed. Waiting for them to be processed...");
			Sleep(Duration.ofSeconds(3));
		}
		shutdownRequested = true;
		log.warn("No orders to process.Shutting down....");
	}

	private void Sleep(Duration duration) {
		try {
			Thread.sleep(duration.toMillis());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
