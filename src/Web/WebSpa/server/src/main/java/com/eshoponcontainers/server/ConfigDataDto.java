package com.eshoponcontainers.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConfigDataDto {
    private String identityUrl;
    private String basketUrl;
    private String marketingUrl;
    private String purchaseUrl;    
    private String signalrHubUrl;
    private String activateCampaignDetailFunction;
    private Boolean useCustomizationData;
}
