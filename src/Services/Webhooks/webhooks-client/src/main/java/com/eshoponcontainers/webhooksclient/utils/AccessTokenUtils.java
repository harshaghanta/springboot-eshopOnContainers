package com.eshoponcontainers.webhooksclient.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;    

    public static String getAccessToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();
            String principalName = oauthToken.getName();
            
            OAuth2AuthorizedClient authorizedClient = applicationContext.getBean(OAuth2AuthorizedClientService.class).loadAuthorizedClient(clientRegistrationId,
                    principalName);
            if (authorizedClient != null) {
                OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
                if (accessToken != null) {
                    return accessToken.getTokenValue();
                }
            }
        }
        return null;
    }

    @Override
    public  void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AccessTokenUtils.applicationContext = applicationContext;
    }    
}
