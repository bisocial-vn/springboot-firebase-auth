package com.firebase.auth.example.configuration;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.firebase.auth.example.entity.TestEntity;

@Configuration
public class InitialConfiguration implements CommandLineRunner {

	@PersistenceContext
	EntityManager entityManager;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		TestEntity testEntity1 = new TestEntity();
		testEntity1.setDesc(UUID.randomUUID().toString());
		entityManager.persist(testEntity1);
	}

}
