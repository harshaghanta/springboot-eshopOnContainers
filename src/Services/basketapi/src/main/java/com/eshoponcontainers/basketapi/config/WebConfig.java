package com.eshoponcontainers.basketapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.reactive.UrlHandlerFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Environment env;

    // @Override
    // public void addCorsMappings(CorsRegistry registry) {
    // String allowedCorsOrigin = env.getProperty("ALLOWED_ORIGINS");
    // String allowedHeaders = env.getProperty("ALLOWED_HEADERS", "*");
    // String allowedMethods = env.getProperty("ALLOWED_METHODS", "*");

    // registry.addMapping("/**").allowedOrigins(allowedCorsOrigin).allowedMethods(allowedMethods)
    // .allowedHeaders(allowedHeaders).allowCredentials(true);
    // }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);
        configurer.setPathMatcher(matcher);

        PathPatternParser patternParser = new PathPatternParser();

        configurer.setPatternParser(patternParser);
        // configurer.setTrailingSlashMatch(true); // Removed: not available in recent Spring versions
    }



}
