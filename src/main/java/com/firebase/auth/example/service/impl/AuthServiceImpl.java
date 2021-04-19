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
import org.springframework.stereotype.Service;

import com.firebase.auth.example.configuration.properties.JwtProperties;
import com.firebase.auth.example.dto.response.TokenResponse;
import com.firebase.auth.example.entity.AccountEntity;
import com.firebase.auth.example.entity.AuthTokenEntity;
import com.firebase.auth.example.repository.AuthTokenRepository;
import com.firebase.auth.example.security.AccountDetails;
import com.firebase.auth.example.service.AuthService;
import com.firebase.auth.example.service.CipherService;
import com.firebase.auth.example.service.TokenService;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;
	private TokenService tokenService;
	private JwtProperties jwtProperties;
	private CipherService cipherService;
	private AuthTokenRepository authTokenRepository;

	public AuthServiceImpl(@Qualifier(BeanIds.AUTHENTICATION_MANAGER) AuthenticationManager authenticationManager,
			TokenService tokenService, JwtProperties jwtProperties, CipherService cipherService,
			AuthTokenRepository authTokenRepository) {
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		this.jwtProperties = jwtProperties;
		this.cipherService = cipherService;
		this.authTokenRepository = authTokenRepository;
	}

	@Override
	public TokenResponse authenticationUser(String emailOrPhone, String password, boolean isRemember)
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
		Instant now = Instant.now();
		Instant accessTokenExpiryDate = now.plus(jwtProperties.getAccessTokenDuration());
		String accessToken = this.tokenService.generateToken(String.valueOf(accountDetails.getAccountId()), claims);
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		tokenResponse.setType(jwtProperties.getType());
		tokenResponse.setAccessTokenExpiryDate(Date.from(accessTokenExpiryDate));
		if (isRemember) {
			String uuid = UUID.randomUUID().toString();
			String refreshToken = cipherService.encryptText(uuid);
			tokenResponse.setRefreshToken(refreshToken);
			Instant refreshTokenExpiryDate = now.plus(jwtProperties.getRefreshTokenDuration());
			this.saveRefreshToken(uuid, Date.from(refreshTokenExpiryDate), accountDetails.getAccountId());
		}
		return tokenResponse;
	}

	private void saveRefreshToken(String uuid, Date expiryDate, Long accountId) {
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setId(accountId);
		AuthTokenEntity authTokenEntity = new AuthTokenEntity(uuid, expiryDate, accountEntity);
		authTokenRepository.save(authTokenEntity);
	}

}
