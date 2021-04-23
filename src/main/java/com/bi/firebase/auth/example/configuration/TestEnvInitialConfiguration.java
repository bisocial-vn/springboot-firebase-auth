package com.bi.firebase.auth.example.configuration;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.bi.firebase.auth.example.entity.AccountEntity;
import com.bi.firebase.auth.example.repository.AccountRepository;

@Configuration
@Profile(value = { "test", "dev" })
public class TestEnvInitialConfiguration implements CommandLineRunner {

	private AccountRepository accountRepository;
	private PasswordEncoder passwordEncoder;

	public TestEnvInitialConfiguration(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
		this.accountRepository = accountRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... args) throws Exception {
		accountRepository.findByEmail("test01@test.test").orElseGet(() -> {
			AccountEntity accountEntity = new AccountEntity();
			accountEntity.setEmail("test01@test.test");
			accountEntity.setPassword(passwordEncoder.encode("secret"));
			return accountRepository.save(accountEntity);
		});
	}

}
