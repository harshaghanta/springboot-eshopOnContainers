package com.eshoponcontainers.catalogapi.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"com.eshoponcontainers.catalogapi.entities", "com.eshoponcontainers.entities"})
public class EntityScanConfig {
    
}
