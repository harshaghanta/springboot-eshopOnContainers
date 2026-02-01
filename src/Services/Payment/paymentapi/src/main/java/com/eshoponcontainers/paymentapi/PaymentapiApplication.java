package com.eshoponcontainers.paymentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(scanBasePackages = "com.eshoponcontainers")
public class PaymentapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentapiApplication.class, args);
	}


	//TODO: High : Identify & Move this bean definition to common place
	@Bean
	public ObjectMapper	 objectMapper() {
		return new ObjectMapper();
	}

}
