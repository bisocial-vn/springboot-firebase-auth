package com.firebase.auth.example.core.jwt;

import java.security.PrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenIssuer {

	private static volatile TokenIssuer INSTANCE = null;

	private TokenIssuer() {
		if (INSTANCE != null) {
			throw new RuntimeException("Already initial.");
		}
	}

	public static TokenIssuer getInstance() {
		if (INSTANCE == null) {
			synchronized (TokenIssuer.class) {
				if (INSTANCE == null) {
					INSTANCE = new TokenIssuer();
				}
			}
		}
		return INSTANCE;
	}

	public String issuanceToken(PrivateKey privateKey, String subject, long durationInMs, Map<String, Object> payload)
			throws RuntimeException {
		if (privateKey == null || privateKey.getEncoded().length < 256l) {
			throw new RuntimeException("Invalid private key. Private key length must atlest 2048 bit.");
		}
		if (durationInMs <= 0) {
			durationInMs = Duration.ofMinutes(5l).toMillis();
		}
		Instant now = Instant.now();
		Instant expirationDate = now.plusMillis(durationInMs);
		JwtBuilder jwtIssuerBuild = Jwts.builder();
		if (subject != null && subject.length() > 0) {
			jwtIssuerBuild.setSubject(subject);
		}
		if (payload != null && !payload.isEmpty()) {
			jwtIssuerBuild.setClaims(payload);
		}
		jwtIssuerBuild.setIssuedAt(Date.from(now)).setExpiration(Date.from(expirationDate));
		jwtIssuerBuild.signWith(privateKey, SignatureAlgorithm.RS256);
		return jwtIssuerBuild.compact();
	}

}
