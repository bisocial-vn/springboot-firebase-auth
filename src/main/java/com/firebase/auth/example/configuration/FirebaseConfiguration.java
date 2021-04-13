package com.firebase.auth.example.configuration;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class FirebaseConfiguration {

	@Value("${app.firebase.credential.path:/test.json}")
	private String firebaseCredentialPath;

	@Bean
	public FirebaseApp initial() {
		GoogleCredentials googleCredentials = null;
		try {
			FileInputStream firebaseCredentialFileInputStream = new FileInputStream(firebaseCredentialPath);
			googleCredentials = GoogleCredentials.fromStream(firebaseCredentialFileInputStream);
		} catch (IOException ex) {
			try {
				log.info("Initial firebase with default. firebaseCredentialPath: {}", firebaseCredentialPath);
				googleCredentials = GoogleCredentials.getApplicationDefault();
			} catch (IOException ex1) {
				throw new RuntimeException("Fail to get google credential for firebase app.", ex);
			}
		}
		FirebaseOptions firebaseOptions = FirebaseOptions.builder().setCredentials(googleCredentials).build();
		FirebaseApp firebaseAppInstance = FirebaseApp.initializeApp(firebaseOptions);

		log.info("Initial Firebase app success.\n\tApp name: {}", firebaseAppInstance.getName());
		log.info("Firebase Databasr URL: {}", firebaseAppInstance.getOptions().getDatabaseUrl());
		log.info("Firebase ProjectID: {}", firebaseAppInstance.getOptions().getProjectId());
		log.info("Firebase Firestore: {}", firebaseAppInstance.getOptions().getStorageBucket());
		return firebaseAppInstance;
	}

}
