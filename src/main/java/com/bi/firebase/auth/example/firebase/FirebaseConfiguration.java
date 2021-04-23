package com.bi.firebase.auth.example.firebase;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.UrlResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
@Profile("!test")
public class FirebaseConfiguration {

	@Value("${app.firebase.credential.uri}")
	private String firebaseCredentialUri;

	@Bean
	public FirebaseApp firebaseApp() {
		GoogleCredentials googleCredentials = null;
		boolean isInitialCredentialFromDefault = false;
		try {
			InputStream firbCredentialInputStream = new UrlResource(firebaseCredentialUri).getInputStream();
			googleCredentials = GoogleCredentials.fromStream(firbCredentialInputStream);
		} catch (IOException ex) {
			try {
				log.info("Fail to initial from credential path: {}", firebaseCredentialUri);
				log.warn("Try to initial firebase with default.");
				googleCredentials = GoogleCredentials.getApplicationDefault();
				isInitialCredentialFromDefault = true;
			} catch (IOException ioEx) {
				log.error("Initial Firebase app fail: ", ioEx);
				log.debug("Config firebase credential path: {}", firebaseCredentialUri);
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
			log.debug("Config firebase credential path: {}", firebaseCredentialUri);
		}

		log.info("Initial Firebase app success.\n\tApp name: {}", firebaseAppInstance.getName());
		log.debug("Credential initial success from : {}",
				isInitialCredentialFromDefault ? "[DEFAULT]" : firebaseCredentialUri);
		log.info("Firebase Databasr URL: {}", firebaseAppInstance.getOptions().getDatabaseUrl());
		log.info("Firebase ProjectID: {}", firebaseAppInstance.getOptions().getProjectId());
		log.info("Firebase Firestore: {}", firebaseAppInstance.getOptions().getStorageBucket());
		return firebaseAppInstance;
	}

}
