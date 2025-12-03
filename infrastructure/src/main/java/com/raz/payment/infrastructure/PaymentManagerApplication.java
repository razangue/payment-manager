package com.raz.payment.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.raz.payment.manager", "com.raz.payment.application",
		"com.raz.payment.infrastructure", "com.raz.payment.domain" })
@EntityScan(basePackages = { "com.raz.payment.manager", "com.raz.payment.application", "com.raz.payment.infrastructure", "domain" })

public class PaymentManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentManagerApplication.class, args);
	}

}
