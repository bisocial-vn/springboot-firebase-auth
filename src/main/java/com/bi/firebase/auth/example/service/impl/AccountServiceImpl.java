package com.bi.firebase.auth.example.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.bi.firebase.auth.example.dto.ProviderId;
import com.bi.firebase.auth.example.dto.request.FirebaseRegisterAccountRequest;
import com.bi.firebase.auth.example.dto.response.TokenResponse;
import com.bi.firebase.auth.example.entity.AccountEntity;
import com.bi.firebase.auth.example.repository.AccountRepository;
import com.bi.firebase.auth.example.service.AccountService;
import com.bi.firebase.auth.example.service.AuthService;
import com.bi.firebase.auth.example.utils.ValidationUtils;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserRecord;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {

	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	private AuthService authService;

	@Override
	public TokenResponse createAccount(@Valid FirebaseRegisterAccountRequest accountRequest, ProviderId providerId)
			throws Exception {
		ValidationUtils.getInstance().validatePassword(accountRequest.getPassword());
		UserRecord firebaseUserRecord;
		try {
			FirebaseAuth faInstance = FirebaseAuth.getInstance();
			FirebaseToken firebaseToken = faInstance.verifyIdToken(accountRequest.getIdToken(), true);
			firebaseToken.getClaims().forEach((key, value) -> {
				log.info("{} - {}", key, value);
			});
			log.info("UID: {}. \nEmail: {}", firebaseToken.getUid(), firebaseToken.getEmail());
			firebaseUserRecord = faInstance.getUser(firebaseToken.getUid());
			for (UserInfo userInfo : firebaseUserRecord.getProviderData()) {
				log.info("Provider ID: {}, UID: {}", userInfo.getProviderId(), userInfo.getUid());
			}
		} catch (FirebaseAuthException firbAuthEx) {
			throw new Exception(firbAuthEx.getMessage());
		} catch (FirebaseException firbEx) {
			log.debug(firbEx.getMessage(), firbEx);
			throw new Exception(
					"The server is busy or the feature is under maintenance. Please try again in a few minutes.");
		}
		String email = firebaseUserRecord.getEmail();
		String phonenumber = firebaseUserRecord.getPhoneNumber();
		if (email == null && phonenumber == null) {
			throw new IllegalArgumentException("Email or Phone must not be empty.");
		}
		AccountEntity accountEntity = new AccountEntity();
		if (StringUtils.hasText(email)) {
			this.validateEmail(email);
			accountEntity.setEmail(firebaseUserRecord.getEmail());
		}
		if (StringUtils.hasText(phonenumber)) {
			this.validatePhone(phonenumber);
			accountEntity.setPhone(firebaseUserRecord.getPhoneNumber());
		}
		accountEntity.setFirebaseUid(firebaseUserRecord.getUid());
		accountEntity.setPassword(passwordEncoder.encode(accountRequest.getPassword()));
		accountRepository.save(accountEntity);

		Map<String, Object> claims = new HashMap<>();

		claims.put("roles", Collections.singleton("ROLE_USER"));
		claims.put("accId", accountEntity.getId());
		return authService.issueNewToken(String.valueOf(accountEntity.getId()), claims, true);
	}

	@Override
	public void validateEmail(String email) throws Exception {
		if (!StringUtils.hasText(email)) {
			throw new IllegalArgumentException("Email is empty.");
		}
		if (!ValidationUtils.getInstance().isValidEmailFormat(email)) {
			throw new IllegalArgumentException("Invalid email format: " + email + " .");
		}
		if (accountRepository.existsByEmail(email)) {
			throw new Exception("Invalid email: " + email + " .");
		}
	}

	@Override
	public void validatePhone(String phone) throws Exception {
		if (!StringUtils.hasText(phone)) {
			throw new IllegalArgumentException("Phone is empty.");
		}
		if (!ValidationUtils.getInstance().isValidPhonenumber(phone)) {
			throw new IllegalArgumentException("Invalid phone format: " + phone + " .");
		}
		if (accountRepository.existsByPhone(phone)) {
			throw new Exception("Invalid phone: " + phone + " .");
		}
	}

}
