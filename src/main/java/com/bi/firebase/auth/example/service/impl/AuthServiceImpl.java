package com.bi.firebase.auth.example.service.impl;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bi.firebase.auth.example.configuration.properties.JwtProperties;
import com.bi.firebase.auth.example.dto.response.TokenResponse;
import com.bi.firebase.auth.example.entity.AccountEntity;
import com.bi.firebase.auth.example.entity.AuthTokenEntity;
import com.bi.firebase.auth.example.repository.AuthTokenRepository;
import com.bi.firebase.auth.example.security.AccountDetails;
import com.bi.firebase.auth.example.service.AuthService;
import com.bi.firebase.auth.example.service.CipherService;
import com.bi.firebase.auth.example.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
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
	public TokenResponse authenticationWithCredential(String emailOrPhone, String password, boolean isRemember)
			throws AuthenticationException {
		UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(emailOrPhone,
				password);
		Authentication authenticated = this.authenticationManager.authenticate(loginToken);
		// if (authenticated.getAuthorities() == null ||
		// authenticated.getAuthorities().isEmpty()) {
		// throw new RuntimeException("Access denied. Empty role.");
		// }
		if (!AccountDetails.class.isInstance(authenticated.getPrincipal())) {
			throw new RuntimeException("Can not get authentication details.");
		}
		AccountDetails accountDetails = (AccountDetails) authenticated.getPrincipal();
		Long accountId = accountDetails.getAccountId();
		Collection<String> roleAsStr = accountDetails.getAuthorities().parallelStream()
				.map((grantAuthor) -> grantAuthor.getAuthority()).collect(Collectors.toSet());
		Map<String, Object> claims = new HashMap<>();
		if (roleAsStr != null && !roleAsStr.isEmpty()) {
			claims.put("roles", roleAsStr);
		}
		claims.put("accId", accountId);
		TokenResponse tokenResponse = issueNewToken(String.valueOf(accountId), claims, isRemember);
		return tokenResponse;
	}

	@Override
	public TokenResponse issueNewToken(String subject, Map<String, Object> claims, boolean isRefresh) {
		Instant now = Instant.now();
		Instant accessTokenExpiryDate = now.plus(jwtProperties.getAccessTokenDuration());
		String accessToken = this.tokenService.generateToken(subject, claims);
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		tokenResponse.setType(jwtProperties.getType());
		tokenResponse.setAccessTokenExpiryDate(Date.from(accessTokenExpiryDate));
		if (isRefresh) {
			String uuid = UUID.randomUUID().toString();
			String refreshToken = cipherService.encryptText(uuid);
			tokenResponse.setRefreshToken(refreshToken);
			Instant refreshTokenExpiryDate = now.plus(jwtProperties.getRefreshTokenDuration());
			tokenResponse.setRefreshTokenExpiryDate(Date.from(refreshTokenExpiryDate));
			this.saveRefreshToken(uuid, Date.from(refreshTokenExpiryDate), Long.valueOf(subject));
		}
		return tokenResponse;
	}

	private void saveRefreshToken(String uuid, Date expiryDate, Long accountId) {
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setId(accountId);
		AuthTokenEntity authTokenEntity = new AuthTokenEntity(uuid, expiryDate, accountEntity);
		authTokenRepository.save(authTokenEntity);
	}

	@Override
	public TokenResponse authentiationWithRefreshToken(String encryptedRefreshToken) throws Exception {
		String decryptedRefreshToken = cipherService.decryptText(encryptedRefreshToken);
		if (!StringUtils.hasText(decryptedRefreshToken)) {
			throw new Exception("Invalid refreshToken.");
		}
		Optional<AuthTokenEntity> authTokenEntity = authTokenRepository.findByRefreshToken(decryptedRefreshToken);
		if (!authTokenEntity.isPresent()) {
			throw new Exception("Invalid refreshToken. Token not found or expired.");
		}
		Map<String, Object> claims = new HashMap<>();
		claims.put("roles", Collections.singleton("USER"));
		Instant now = Instant.now();
		Instant accessTokenExpiryDate = now.plus(jwtProperties.getAccessTokenDuration());
		String accessToken = this.tokenService.generateToken(String.valueOf(authTokenEntity.get().getAccount().getId()),
				claims);
		TokenResponse tokenResponse = new TokenResponse();
		tokenResponse.setAccessToken(accessToken);
		tokenResponse.setType(jwtProperties.getType());
		tokenResponse.setAccessTokenExpiryDate(Date.from(accessTokenExpiryDate));
		return tokenResponse;
	}

	@Override
	@Transactional
	public void deleteRefreshToken(String encryptedRefreshToken) {
		String decryptedRefreshToken = cipherService.decryptText(encryptedRefreshToken);
		if (!StringUtils.hasText(decryptedRefreshToken)) {
			throw new RuntimeException("Invalid refreshToken.");
		}
		Optional<AuthTokenEntity> authTokenEntity = authTokenRepository.findByRefreshToken(decryptedRefreshToken);
		if (!authTokenEntity.isPresent()) {
			throw new RuntimeException("Invalid refreshToken. Token not found or expired.");
		}
		int deletedCount = authTokenRepository.deleteByRefreshToken(decryptedRefreshToken);
		log.debug("Deleted refreshToken[{}] count: {}", decryptedRefreshToken, deletedCount);
	}

	@Override
	public void deleteAllAccountRefreshToken(Long accountId) {
		if (accountId == null) {
			throw new IllegalArgumentException("Invalid AccountId: " + accountId);
		}
		int deletedRow = authTokenRepository.deleteByAccountId(accountId);
		log.debug("Delete {} refreshToken belong to Account with ID: {}.", deletedRow, accountId);
	}

}
