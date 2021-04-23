package com.bi.firebase.auth.example.service;

import java.util.Map;

public interface TokenService {

	String generateToken(String subject, Map<String, Object> claims);

	Map<String, Object> parseClaimsToken(String token) throws Exception;

}
