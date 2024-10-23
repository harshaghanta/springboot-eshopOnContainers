package com.eshoponcontainers.catalogapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
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

    @Value("${allowedCorsOrigin}")
    private String allowedCorsOrigin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {    
        log.info("Adding CORS mappings for origin:", allowedCorsOrigin);
        registry.addMapping("/**").allowedOrigins(allowedCorsOrigin).allowedMethods("*")
                .allowedHeaders("*");
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);
    }
}
