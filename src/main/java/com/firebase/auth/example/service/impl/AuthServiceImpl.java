package com.firebase.auth.example.service.impl;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import com.firebase.auth.example.configuration.properties.JwtProperties;
import com.firebase.auth.example.dto.response.TokenResponse;
import com.firebase.auth.example.security.AccountDetails;
import com.firebase.auth.example.service.AuthService;
import com.firebase.auth.example.service.TokenService;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;
	private TokenService tokenService;
	private JwtProperties jwtProperties;

	public AuthServiceImpl(@Qualifier(BeanIds.AUTHENTICATION_MANAGER) AuthenticationManager authenticationManager,
			TokenService tokenService, JwtProperties jwtProperties) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.jwtProperties = jwtProperties;
	}

	@Override
	public String authenticationUser(String emailOrPhone, String password, boolean isRemember)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(emailOrPhone,
				password);
		Authentication authenticated = this.authenticationManager.authenticate(loginToken);
//		if (authenticated.getAuthorities() == null || authenticated.getAuthorities().isEmpty()) {
//			throw new RuntimeException("Access denied. Empty role.");
//		}
		if (!AccountDetails.class.isInstance(authenticated.getPrincipal())) {
			throw new RuntimeException("Can not get authentication details.");
		}
		AccountDetails accountDetails = (AccountDetails) authenticated.getPrincipal();
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", accountDetails.getAuthorities());
		Instant tokenExpiryDate = Instant.now().plus(jwtProperties.getAccessTokenDuration());
		String accessToken = this.tokenService.generateToken(String.valueOf(accountDetails.getAccountId()), claims);
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		tokenResponse.setType(jwtProperties.getType());
		tokenResponse.setExpiryDate(Date.from(tokenExpiryDate));
		if (isRemember) {
			String uuid = UUID.randomUUID().toString();
			String refreshToken = this.encrypt(uuid);
		}
		return UUID.randomUUID().toString();
	}

	private String encrypt(String input) {
		TextEncryptor textEncryptor = Encryptors.text(jwtProperties.getRefreshTokenKey(),
				KeyGenerators.string().generateKey());
		return textEncryptor.encrypt(input);
	}

	private String decrypt(String input) {
		TextEncryptor textEncryptor = Encryptors.text(jwtProperties.getRefreshTokenKey(),
				KeyGenerators.string().generateKey());
		return textEncryptor.decrypt(input);
	}

}
