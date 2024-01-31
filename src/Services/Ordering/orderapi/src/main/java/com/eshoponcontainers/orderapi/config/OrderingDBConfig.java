package com.eshoponcontainers.orderapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "order.datasource")
@Data
@NoArgsConstructor
public class OrderingDBConfig {
    private String url;
    private String username;
    private String password;
}
