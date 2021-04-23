package com.bi.firebase.auth.example.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.bi.firebase.auth.example.dto.JwtTokenPrincipal;

public final class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 5525073995766314191L;

	private String tokenCredential;
	private JwtTokenPrincipal jwtTokenPrincipal;

	public JwtAuthenticationToken(String tokenCredential) {
		super(null);
		this.tokenCredential = tokenCredential;
		this.setAuthenticated(false);
	}

	public JwtAuthenticationToken(JwtTokenPrincipal jwtTokenPrincipal, Object details,
			Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.jwtTokenPrincipal = jwtTokenPrincipal;
		this.setDetails(details);
		this.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return tokenCredential;
	}

	@Override
	public Object getPrincipal() {
		return jwtTokenPrincipal;
	}

}
