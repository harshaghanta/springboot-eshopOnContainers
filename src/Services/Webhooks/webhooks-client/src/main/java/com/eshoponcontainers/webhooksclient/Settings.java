package com.eshoponcontainers.webhooksclient;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "settings")
public class Settings {

    private String token;
    private String identityUrl;
    private String callbackUrl;
    private String webhookUrl;
    private String selfUrl;
    private boolean validateToken;
}
