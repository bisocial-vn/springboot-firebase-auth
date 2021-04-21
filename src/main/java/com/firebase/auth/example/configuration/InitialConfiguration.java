package com.firebase.auth.example.configuration;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.firebase.auth.example.configuration.properties.JwtProperties;
import com.firebase.auth.example.entity.TestEntity;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class InitialConfiguration implements CommandLineRunner {

	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	JwtProperties jwtConfig;

	@Override
	@Transactional
	public void run(String... args) throws Exception {
		TestEntity testEntity1 = new TestEntity();
		testEntity1.setDesception(UUID.randomUUID().toString());
		testEntity1.setTestDate(new Date());
		entityManager.persist(testEntity1);
		log.info(testEntity1.toString());
		log.info(jwtConfig.toString());
	}

}
