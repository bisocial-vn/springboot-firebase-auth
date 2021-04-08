package com.firebase.auth.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirebaseAuthBeTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(FirebaseAuthBeTestApplication.class, args);
	}

}
// set MAVEN_OPTS=-Dhttp.proxyHost=10.225.3.1 -Dhttp.proxyPort=3128 -Dhttps.proxyHost=10.225.3.1 -Dhttps.proxyPort=3128