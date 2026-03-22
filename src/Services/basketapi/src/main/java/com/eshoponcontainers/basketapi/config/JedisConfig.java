package com.eshoponcontainers.basketapi.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@Slf4j
public class JedisConfig {

    @Bean
    @Scope(value = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
    // @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Jedis jedis(JedisPool jedisPool) {


        return jedisPool.getResource();
    }

    @Bean
    public JedisPoolConfig buildPoolConfig() {
        final JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(128);
        poolConfig.setMaxIdle(128);
        poolConfig.setMinIdle(16);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        poolConfig.setJmxEnabled(false);
        poolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
        poolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        poolConfig.setNumTestsPerEvictionRun(3);
        poolConfig.setBlockWhenExhausted(true);
        return poolConfig;
    }

    @Bean
    public JedisPool jedisPool(Environment env, JedisPoolConfig poolConfig) {
        String host = env.getProperty("REDIS_HOST_NAME");
        String port = env.getProperty("REDIS_HOST_PORT");
        String timeout = env.getProperty("REDIS_TIMEOUT", "2000");
        Assert.notNull(host, "REDIS_HOST_NAME environment variable must be set");
        Assert.notNull(port, "REDIS_HOST_PORT environment variable must be set");
        Assert.notNull(timeout, "REDIS_TIMEOUT environment variable must be set");

        log.info("Creating JedisPool bean for host: {}", env.getProperty("REDIS_HOST_NAME"));
        String secretsPath = env.getProperty("SECRETS_PATH", "/vault/secrets");
        String password = readSecret(secretsPath + "/REDIS_PASSWORD");

        return new JedisPool(poolConfig, host, Integer.parseInt(port), Integer.parseInt(timeout), password);
    }

    private String readSecret(String filePath) {
        try {
            // .trim() is crucial because K8s secret files often have a trailing newline
            return Files.readString(Paths.get(filePath)).trim();
        } catch (IOException e) {
            throw new RuntimeException("Could not read database secret at " + filePath, e);
        }
    }

}
