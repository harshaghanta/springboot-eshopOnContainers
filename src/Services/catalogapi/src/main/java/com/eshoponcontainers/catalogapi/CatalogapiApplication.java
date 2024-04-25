package com.eshoponcontainers.catalogapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan(basePackages = {"com.eshoponcontainers.repositories", "com.eshoponcontainers.catalogapi", "com.eshoponcontainers.services", "com.eshoponcontainers.eventbus", "com.eshoponcontainers.eventbus.rabbitmq" })
@EnableJpaRepositories(basePackages = {"com.eshoponcontainers.repositories", "com.eshoponcontainers.catalogapi" })
public class CatalogapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CatalogapiApplication.class, args);
	}
}
