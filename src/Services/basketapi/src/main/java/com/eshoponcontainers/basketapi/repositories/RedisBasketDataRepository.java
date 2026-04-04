package com.eshoponcontainers.basketapi.repositories;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eshoponcontainers.basketapi.model.BasketItem;
import com.eshoponcontainers.basketapi.model.CustomerBasket;
import com.eshoponcontainers.basketapi.model.StoredBasketItem;
import com.eshoponcontainers.basketapi.model.StoredCustomerBasket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisBasketDataRepository {

    private static final String BASKET_KEY_PREFIX = "basket:";

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;
    private final BasketNotificationStateRepository notificationStateRepository;

    public boolean deleteBasket(String id) {
        long deletedKeys;
        try (Jedis jedis = jedisPool.getResource()) {
            deletedKeys = jedis.del(basketKey(id), id);
        }
        notificationStateRepository.clearNotifications(id);
        notificationStateRepository.clearUserContext(id);
        return deletedKeys > 0;
    }

    public List<String> getUsers() {
        Set<String> users = new HashSet<>();

        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> basketKeys = jedis.keys(BASKET_KEY_PREFIX + "*");
            if (basketKeys != null) {
                basketKeys.stream()
                        .map(key -> key.substring(BASKET_KEY_PREFIX.length()))
                        .forEach(users::add);
            }

            Set<String> legacyKeys = jedis.keys("*");
            if (legacyKeys != null) {
                legacyKeys.stream()
                        .filter(key -> !key.startsWith(BASKET_KEY_PREFIX))
                        .filter(key -> !key.startsWith("basket-price-notification:"))
                        .filter(key -> !key.startsWith("basket-user-context:"))
                        .filter(key -> !key.contains(":"))
                        .forEach(users::add);
            }
        }

        return users.stream().toList();
    }

    public CustomerBasket getBasket(String customerId) {
        String strBasket;
        try (Jedis jedis = jedisPool.getResource()) {
            strBasket = jedis.get(basketKey(customerId));
            if (!StringUtils.hasText(strBasket)) {
                strBasket = jedis.get(customerId);
            }
        }

        try {
            if (!StringUtils.hasText(strBasket)) {
                return null;
            }
            StoredCustomerBasket storedBasket = objectMapper.copy()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .readValue(strBasket, StoredCustomerBasket.class);
            return mapToCustomerBasket(storedBasket);
        } catch (JsonProcessingException e) {
            log.error("Error occured while deserializing basket:" + customerId, e);
            return null;
        }
    }

    public CustomerBasket updateBasket(CustomerBasket basket) {
        return updateBasket(basket, null);
    }

    public CustomerBasket updateBasket(CustomerBasket basket, String username) {

        try {
            StoredCustomerBasket storedBasket = mapToStoredBasket(basket);

            String value;
            try (Jedis jedis = jedisPool.getResource()) {
                value = jedis.set(basketKey(basket.getBuyerId()), objectMapper.writeValueAsString(storedBasket));
                jedis.del(basket.getBuyerId());
            }
            notificationStateRepository.saveUserContext(basket.getBuyerId(), username);
            notificationStateRepository.clearNotificationsNotInBasket(
                    basket.getBuyerId(),
                    storedBasket.getItems().stream().map(StoredBasketItem::getProductId).collect(Collectors.toSet()));

            if ("OK".equals(value)) {
                log.info("Basket item persisted successfully.");
                return getBasket(basket.getBuyerId());
            } else {
                log.info("Problem occured persisting the item.");
            }

        } catch (JsonProcessingException e) {
            log.error("Error occured while updating basket for buyer:" + basket.getBuyerId(), e);
        }
        return null;
    }

    private String basketKey(String buyerId) {
        return BASKET_KEY_PREFIX + buyerId;
    }

    private StoredCustomerBasket mapToStoredBasket(CustomerBasket basket) {
        StoredCustomerBasket storedBasket = new StoredCustomerBasket(basket.getBuyerId());
        if (basket.getItems() == null) {
            return storedBasket;
        }

        storedBasket.setItems(basket.getItems().stream()
                .map(item -> new StoredBasketItem(
                        StringUtils.hasText(item.getId()) ? item.getId() : String.valueOf(item.getProductId()),
                        item.getProductId(),
                        item.getQuantity()))
                .toList());
        return storedBasket;
    }

    private CustomerBasket mapToCustomerBasket(StoredCustomerBasket storedBasket) {
        CustomerBasket basket = new CustomerBasket(storedBasket.getBuyerId());
        if (storedBasket.getItems() == null) {
            return basket;
        }

        basket.setItems(storedBasket.getItems().stream()
                .map(item -> {
                    BasketItem basketItem = new BasketItem();
                    basketItem.setId(StringUtils.hasText(item.getId()) ? item.getId() : String.valueOf(item.getProductId()));
                    basketItem.setProductId(item.getProductId());
                    basketItem.setQuantity(item.getQuantity());
                    return basketItem;
                })
                .toList());
        return basket;
    }
}
