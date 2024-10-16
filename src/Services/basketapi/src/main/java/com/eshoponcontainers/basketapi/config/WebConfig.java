package com.eshoponcontainers.basketapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://host.docker.internal:8080").allowedMethods("*")
                .allowedHeaders("*").allowCredentials(true);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AntPathMatcher matcher = new AntPathMatcher();
        matcher.setCaseSensitive(false);        
        configurer.setPathMatcher(matcher);
        
        PathPatternParser patternParser = new PathPatternParser();
        patternParser.setMatchOptionalTrailingSeparator(true); // Ignore trailing slashes
        configurer.setPatternParser(patternParser);
    }
}
