package com.eshoponcontainers.basketapi.integrationevents.eventhandlers;

import java.util.List;

import org.springframework.stereotype.Component;

import com.eshoponcontainers.basketapi.integrationevents.events.ProductPriceChangedIntegrationEvent;
import com.eshoponcontainers.basketapi.model.BasketItem;
import com.eshoponcontainers.basketapi.model.CustomerBasket;
import com.eshoponcontainers.basketapi.repositories.RedisBasketDataRepository;
import com.eshoponcontainers.eventbus.abstractions.IntegrationEventHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductPriceChangedEventHandler implements IntegrationEventHandler<ProductPriceChangedIntegrationEvent> {

  private final RedisBasketDataRepository basketDataRepository;

  @Override
  public Runnable handle(ProductPriceChangedIntegrationEvent event) {

    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        log.info("Handling integration event: {} at {}  - ({})", event.getId(), "BasketAPI", event);
        List<String> users = basketDataRepository.getUsers();
        if (users != null) {
          for (String user : users) {
            CustomerBasket basket = basketDataRepository.getBasket(user);
            updatePriceInBasketItems(event.getProductId(), event.getNewPrice(), event.getOldPrice(), basket);
          }
        }
      }
    };

    return runnable;
  }

  private void updatePriceInBasketItems(int productId, double newPrice, double oldPrice, CustomerBasket basket) {
    List<BasketItem> itemsToUpdate = basket.getItems().stream().filter(item -> item.getProductId() == productId)
        .toList();
    if (itemsToUpdate != null) {
      log.info("---ProductPriceChangedIntegrationEventHandler - Updating items in basket for user: {}",
          basket.getBuyerId());
      for (BasketItem basketItem : itemsToUpdate) {
        if (basketItem.getUnitPrice() == oldPrice) {
          double originalPrice = basketItem.getUnitPrice();
          basketItem.setUnitPrice(newPrice);
          basketItem.setOldUnitPrice(originalPrice);
        }
      }
      basketDataRepository.updateBasket(basket);
    }
  }
}
