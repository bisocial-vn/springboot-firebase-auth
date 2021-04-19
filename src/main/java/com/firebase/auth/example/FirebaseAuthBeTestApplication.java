package com.firebase.auth.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;

@SpringBootApplication(exclude = { JmsAutoConfiguration.class })
public class FirebaseAuthBeTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirebaseAuthBeTestApplication.class, args);
	}

}