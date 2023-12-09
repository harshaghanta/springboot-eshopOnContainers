package com.eshoponcontainers.basketapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;

@Configuration
public class JedisConfig {

    // @Autowired
    // private RedisProperties redisProperties;

    @Bean
    public Jedis jedis() {
        return new Jedis();
    }
}
