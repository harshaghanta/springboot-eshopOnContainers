package com.eshoponcontainers.webhooksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eshoponcontainers")
public class WebhooksApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebhooksApiApplication.class, args);
	}
}
