package com.eshoponcontainers.orderapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebMvc
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }

    	 @Bean
    public CorsFilter corsFilter() {

        String allowedCorsOrigin = env.getProperty("ALLOWED_ORIGINS");
        String allowedHeaders = env.getProperty("ALLOWED_HEADERS", "*");
        String allowedMethods = env.getProperty("ALLOWED_METHODS", "*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
		
        config.addAllowedOrigin(allowedCorsOrigin);
        config.addAllowedHeader(allowedHeaders);
        config.addAllowedMethod(allowedMethods);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

}
