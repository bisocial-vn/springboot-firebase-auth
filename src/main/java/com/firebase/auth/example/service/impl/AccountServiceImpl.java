package com.firebase.auth.example.service.impl;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.firebase.auth.example.dto.request.RegisterAccountRequest;
import com.firebase.auth.example.entity.AccountEntity;
import com.firebase.auth.example.repository.AccountRepository;
import com.firebase.auth.example.service.AccountService;
import com.firebase.auth.example.utils.ValidationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	AccountRepository accountRepository;

	@Override
	public AccountEntity createAccount(@Valid RegisterAccountRequest accountRequest) throws Exception {
		ValidationUtils.getInstance().validatePassword(accountRequest.getPassword());
		try {
			FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(accountRequest.getIdToken(), true);
			firebaseToken.getClaims().forEach((key, value) -> {
				log.info("{} - {}", key, value);
			});
			log.info("UID: {}. \nEmail: {}", firebaseToken.getUid(), firebaseToken.getEmail());
			AccountEntity accountEntity = new AccountEntity();
			accountEntity.setEmail(firebaseToken.getEmail());
			accountEntity.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
			return accountRepository.save(accountEntity);
		} catch (FirebaseAuthException firbEx) {
			throw new BadCredentialsException(firbEx.getMessage());
		}
	}

}
