package com.firebase.auth.example.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.BeanIds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import com.firebase.auth.example.security.AccountDetails;
import com.firebase.auth.example.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

	private AuthenticationManager authenticationManager;

	public AuthServiceImpl(@Qualifier(BeanIds.AUTHENTICATION_MANAGER) AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
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
		return UUID.randomUUID().toString();
	}

}
