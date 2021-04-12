package com.firebase.auth.example.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfiguration {

	@Value("${app.firebase.credential.path}")
	private String firebaseCredentialPath;

	@PostConstruct
	public void initial() throws Exception {
		try {
			FileInputStream firebaseCredentialFileInputStream = new FileInputStream(firebaseCredentialPath);
			FirebaseOptions firebaseOptions = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(firebaseCredentialFileInputStream)).build();
			FirebaseApp.initializeApp(firebaseOptions);
		} catch (FileNotFoundException ex) {
			throw new Exception("Initial firebase fail. " + ex.getMessage());
		}
	}

}
