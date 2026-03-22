package com.eshoponcontainers.catalogapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.AntPathMatcher;
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
    public void addCorsMappings(CorsRegistry registry) {    
        String allowedCorsOrigin = env.getProperty("ALLOWED_ORIGINS");
        String allowedHeaders = env.getProperty("ALLOWED_HEADERS", "*");
        String allowedMethods = env.getProperty("ALLOWED_METHODS", "*");

        log.info("Adding CORS mappings for origin:", allowedCorsOrigin);

        registry.addMapping("/**").allowedOrigins(allowedCorsOrigin).allowedMethods(allowedMethods)
                .allowedHeaders(allowedHeaders);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }
}
