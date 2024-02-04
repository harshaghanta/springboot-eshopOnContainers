package com.eshoponcontainers.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.NoArgsConstructor;

@Configuration
@ConfigurationProperties(prefix = "config-data")
@Data
@NoArgsConstructor
public class ConfigData {

    private String identityUrl;
    private String basketUrl;
    private String marketingUrl;
    private String purchaseUrl;    
    private String signalrHubUrl;
    private String activateCampaignDetailFunction;
    private Boolean useCustomizationData;

}
