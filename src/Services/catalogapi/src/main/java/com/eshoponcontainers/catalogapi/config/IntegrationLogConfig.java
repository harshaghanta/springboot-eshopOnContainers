package com.eshoponcontainers.catalogapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eshoponcontainers.repositories.IntegrationEventLogRepository;
import com.eshoponcontainers.services.IntegrationEventLogService;
import com.eshoponcontainers.services.impl.DefaultIntegrationEventLogService;

@Configuration
public class IntegrationLogConfig {
    
    @Bean
    public IntegrationEventLogService integrationEventLogService(IntegrationEventLogRepository integrationEventLogRepository) {
        return new DefaultIntegrationEventLogService(integrationEventLogRepository);
    }
}
