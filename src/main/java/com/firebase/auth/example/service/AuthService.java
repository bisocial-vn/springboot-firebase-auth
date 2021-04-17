package com.firebase.auth.example.service;

import org.springframework.security.core.AuthenticationException;

public interface AuthService {

	String authenticationUser(String emailOrPhone, String password, boolean isRemember) throws AuthenticationException;

}
