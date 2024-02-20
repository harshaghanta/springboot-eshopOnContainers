package com.eshoponcontainers.basketapi.services;

import java.util.Map;

import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class IdentityService {

    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
            return token.getName();
        }
        Principal principal = (Principal) authentication.getPrincipal();
        return principal.getName();
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
            Map<String, Object> tokenAttributes = token.getTokenAttributes();
            return (String) tokenAttributes.get("preferred_username");
        }
        Principal principal = (Principal) authentication.getPrincipal();
        return principal.getName();
    }

    public String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
            Map<String, Object> tokenAttributes = token.getTokenAttributes();
            return (String) tokenAttributes.get("email");
        }
        Principal principal = (Principal) authentication.getPrincipal();
        return principal.getName();
    }

}
