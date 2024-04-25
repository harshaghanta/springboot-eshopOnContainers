package com.eshoponcontainers.basketapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eshoponcontainers")
public class BasketapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BasketapiApplication.class, args);		
	}

}
