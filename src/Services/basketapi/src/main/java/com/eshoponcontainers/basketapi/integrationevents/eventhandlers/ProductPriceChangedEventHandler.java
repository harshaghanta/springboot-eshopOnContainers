package com.eshoponcontainers.basketapi.integrationevents.eventhandlers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.basketapi.integrationevents.events.BasketProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.basketapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.basketapi.model.CustomerBasket;
import com.eshoponcontainers.basketapi.model.BasketPriceChangedNotification;
import com.eshoponcontainers.basketapi.repositories.BasketNotificationStateRepository;
import com.eshoponcontainers.basketapi.repositories.RedisBasketDataRepository;
import com.eshoponcontainers.eventbus.abstractions.EventBus;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductPriceChangedEventHandler implements IntegrationEventHandler<ProductPriceChangedIntegrationEvent> {

  private final RedisBasketDataRepository basketDataRepository;
  private final BasketNotificationStateRepository notificationStateRepository;
  private final EventBus eventBus;

  @Override
  public void handle(ProductPriceChangedIntegrationEvent event) {

    log.info("Handling integration event: {} at {}  - ({})", event.getId(), "BasketAPI", event);
    List<String> users = basketDataRepository.getUsers();
    if (users != null) {
      for (String user : users) {
        CustomerBasket basket = basketDataRepository.getBasket(user);
        trackPriceChange(event.getProductId(), event.getNewPrice().doubleValue(), event.getOldPrice().doubleValue(), basket);
      }
    }

  }

  private void trackPriceChange(int productId, double newPrice, double oldPrice, CustomerBasket basket) {
    if (basket == null || basket.getItems() == null) {
      return;
    }

    boolean productExistsInBasket = basket.getItems().stream().anyMatch(item -> item.getProductId() == productId);
    if (!productExistsInBasket) {
      return;
    }

    String username = notificationStateRepository.getUsername(basket.getBuyerId());
    if (username == null || username.isBlank()) {
      log.warn("Skipping price change notification for buyer {} because username context is missing", basket.getBuyerId());
      return;
    }

    BasketPriceChangedNotification notification = notificationStateRepository.upsertNotification(
        basket.getBuyerId(),
        username,
        productId,
        oldPrice,
        newPrice);

    if (notification == null) {
      return;
    }

    eventBus.publish(new BasketProductPriceChangedIntegrationEvent(
        basket.getBuyerId(),
        username,
        productId,
        notification.getPreviousPrice(),
        notification.getCurrentPrice()));
  }
}
