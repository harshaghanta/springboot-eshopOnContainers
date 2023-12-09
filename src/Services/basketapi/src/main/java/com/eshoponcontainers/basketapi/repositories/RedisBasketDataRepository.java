package com.eshoponcontainers.basketapi.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.eshoponcontainers.basketapi.model.CustomerBasket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RedisBasketDataRepository {

    private final Jedis jedis;
    private final ObjectMapper objectMapper;

    public boolean deleteBasket(String id) {
        return jedis.del(id) == 1;
    }

    public List<String> getUsers() {        
        return jedis.keys("*").stream().toList();        
    }

    public CustomerBasket getBasket(String customerId) {
        String strBasket = jedis.get(customerId);
        CustomerBasket basket = null;
        try {
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            basket = objectMapper.readValue(strBasket, CustomerBasket.class);
            if (!StringUtils.hasText(strBasket))
                return null;
        } catch (JsonProcessingException e) {

            log.error("Error occured while deserializing basket:" + customerId, e);
        }
        return basket;
    }

    public CustomerBasket updateBasket(CustomerBasket basket) {

        try {

            String value = jedis.set(basket.getBuyerId(), objectMapper.writeValueAsString(basket));
            if ( value.equals("OK")) {
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
}
