package com.firebase.auth.example.firebase;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.UrlResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfiguration {

	@Value("${app.firebase.credential.path}")
	private String firebaseCredentialPath;

	@Bean
	public FirebaseApp firebaseApp() {
		GoogleCredentials googleCredentials = null;
		try {
			InputStream firbCredentialInputStream = new UrlResource(firebaseCredentialPath).getInputStream();
			googleCredentials = GoogleCredentials.fromStream(firbCredentialInputStream);
		} catch (IOException ex) {
			try {
				log.info("Initial firebase with default. firebaseCredentialPath: {}", firebaseCredentialPath);
				googleCredentials = GoogleCredentials.getApplicationDefault();
			} catch (IOException ioEx) {
				log.error("Initial Firebase app fail: ", ioEx);
				log.debug("Config firebase credential path: {}", firebaseCredentialPath);
			}
		}
		if (googleCredentials == null) {
			return null;
		}

		FirebaseApp firebaseAppInstance = null;
		try {
			FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
			firebaseAppInstance = FirebaseApp.initializeApp(firebaseOptions);
		} catch (Exception e) {
			log.error("Initial Firebase app fail: ", e);
			log.debug("Config firebase credential path: {}", firebaseCredentialPath);
		}

		log.info("Initial Firebase app success.\n\tApp name: {}", firebaseAppInstance.getName());
		log.info("Firebase Databasr URL: {}", firebaseAppInstance.getOptions().getDatabaseUrl());
		log.info("Firebase ProjectID: {}", firebaseAppInstance.getOptions().getProjectId());
		log.info("Firebase Firestore: {}", firebaseAppInstance.getOptions().getStorageBucket());
		return firebaseAppInstance;
	}

}
