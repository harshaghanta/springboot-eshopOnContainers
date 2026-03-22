package com.eshoponcontainers.orderingbackgroundtasks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@EnableScheduling
@EntityScan(basePackages = { "com.eshoponcontainers.entities" })
@Slf4j
@EnableJpaRepositories(basePackages = { "com.eshoponcontainers" })
@SpringBootApplication(scanBasePackages = "com.eshoponcontainers")
public class OrderingBackgroundtasksApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderingBackgroundtasksApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}
}
