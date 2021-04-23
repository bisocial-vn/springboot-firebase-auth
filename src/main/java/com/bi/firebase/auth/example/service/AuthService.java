package com.bi.firebase.auth.example.service;

import org.springframework.security.core.AuthenticationException;

import com.bi.firebase.auth.example.dto.response.TokenResponse;

public interface AuthService {

	TokenResponse authenticationWithCredential(String emailOrPhone, String password, boolean isRemember)
			throws AuthenticationException;

	TokenResponse authentiationWithRefreshToken(String encryptedRefreshToken) throws Exception;

	void deleteRefreshToken(String encryptedRefreshToken);

	void deleteAllAccountRefreshToken(Long accountId);
}
