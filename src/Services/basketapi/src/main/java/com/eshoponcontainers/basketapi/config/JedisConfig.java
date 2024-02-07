package com.eshoponcontainers.basketapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

@Configuration
@Slf4j
public class JedisConfig {

    @Value("${basketdata.host}")
    private String host;
    
    @Bean
    public Jedis jedis() {
        log.info("Printing basketdata host: {}", host);

        var jedis = new Jedis(host, 6379);
        return jedis;
    }
}
