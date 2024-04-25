package com.eshoponcontainers.paymentapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.eshoponcontainers")
public class PaymentapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentapiApplication.class, args);
	}

}
