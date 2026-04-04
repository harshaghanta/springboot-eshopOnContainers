package com.eshoponcontainers.basketapi.repositories;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eshoponcontainers.basketapi.model.BasketPriceChangedNotification;
import com.eshoponcontainers.basketapi.model.BasketUserContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BasketNotificationStateRepository {

    private static final String USER_CONTEXT_PREFIX = "basket-user-context:";
    private static final String PRICE_NOTIFICATION_PREFIX = "basket-price-notification:";

    private final JedisPool jedisPool;
    private final ObjectMapper objectMapper;

    public void saveUserContext(String buyerId, String username) {
        if (!StringUtils.hasText(buyerId) || !StringUtils.hasText(username)) {
            return;
        }

        writeJson(userContextKey(buyerId), new BasketUserContext(buyerId, username));
    }

    public String getUsername(String buyerId) {
        BasketUserContext context = readJson(userContextKey(buyerId), BasketUserContext.class);
        return context != null ? context.getUsername() : null;
    }

    public BasketPriceChangedNotification upsertNotification(String buyerId, String username, int productId,
            double previousPrice, double currentPrice) {
        if (!StringUtils.hasText(buyerId) || !StringUtils.hasText(username)) {
            return null;
        }

        BasketPriceChangedNotification existing = getNotification(buyerId, productId);
        if (existing != null && Double.compare(existing.getLastNotifiedPrice(), currentPrice) == 0) {
            return null;
        }

        double baselinePrice = existing != null ? existing.getCurrentPrice() : previousPrice;
        BasketPriceChangedNotification notification = new BasketPriceChangedNotification(
                buyerId,
                username,
                productId,
                baselinePrice,
                currentPrice,
                currentPrice,
                System.currentTimeMillis());

        writeJson(notificationKey(buyerId, productId), notification);
        saveUserContext(buyerId, username);
        return notification;
    }

    public BasketPriceChangedNotification getNotification(String buyerId, int productId) {
        return readJson(notificationKey(buyerId, productId), BasketPriceChangedNotification.class);
    }

    public Map<Integer, BasketPriceChangedNotification> getNotifications(String buyerId) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys(notificationKeyPrefix(buyerId) + "*");
            if (keys == null || keys.isEmpty()) {
                return Collections.emptyMap();
            }

            Map<Integer, BasketPriceChangedNotification> notifications = new HashMap<>();
            for (String key : keys) {
                BasketPriceChangedNotification notification = readJson(jedis, key, BasketPriceChangedNotification.class);
                if (notification != null) {
                    notifications.put(notification.getProductId(), notification);
                }
            }
            return notifications;
        }
    }

    public void clearNotificationsNotInBasket(String buyerId, Set<Integer> activeProductIds) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys(notificationKeyPrefix(buyerId) + "*");
            if (keys == null || keys.isEmpty()) {
                return;
            }

            Set<Integer> products = activeProductIds != null ? activeProductIds : new HashSet<>();
            for (String key : keys) {
                BasketPriceChangedNotification notification = readJson(jedis, key, BasketPriceChangedNotification.class);
                if (notification == null) {
                    continue;
                }
                if (!products.contains(notification.getProductId())) {
                    jedis.del(key);
                }
            }
        }
    }

    public void clearNotifications(String buyerId) {
        try (Jedis jedis = jedisPool.getResource()) {
            Set<String> keys = jedis.keys(notificationKeyPrefix(buyerId) + "*");
            if (keys != null && !keys.isEmpty()) {
                jedis.del(keys.toArray(String[]::new));
            }
        }
    }

    public void clearUserContext(String buyerId) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(userContextKey(buyerId));
        }
    }

    private String userContextKey(String buyerId) {
        return USER_CONTEXT_PREFIX + buyerId;
    }

    private String notificationKeyPrefix(String buyerId) {
        return PRICE_NOTIFICATION_PREFIX + buyerId + ":";
    }

    private String notificationKey(String buyerId, int productId) {
        return notificationKeyPrefix(buyerId) + productId;
    }

    private void writeJson(String key, Object value) {
        try (Jedis jedis = jedisPool.getResource()) {
            writeJson(jedis, key, value);
        }
    }

    private void writeJson(Jedis jedis, String key, Object value) {
        try {
            jedis.set(key, objectMapper.writeValueAsString(value));
        } catch (JsonProcessingException e) {
            log.error("Error serializing notification state for key {}", key, e);
        }
    }

    private <T> T readJson(String key, Class<T> type) {
        try (Jedis jedis = jedisPool.getResource()) {
            return readJson(jedis, key, type);
        }
    }

    private <T> T readJson(Jedis jedis, String key, Class<T> type) {
        String content = jedis.get(key);
        if (!StringUtils.hasText(content)) {
            return null;
        }

        try {
            return objectMapper.readValue(content, type);
        } catch (JsonProcessingException e) {
            log.error("Error deserializing notification state for key {}", key, e);
            return null;
        }
    }
}