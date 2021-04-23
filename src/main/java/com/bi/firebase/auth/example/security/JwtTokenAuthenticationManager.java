package com.bi.firebase.auth.example.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.bi.firebase.auth.example.constant.CommonConstant;
import com.bi.firebase.auth.example.dto.JwtTokenPrincipal;
import com.bi.firebase.auth.example.service.TokenService;

public final class JwtTokenAuthenticationManager implements AuthenticationProvider, AuthenticationManager {

	private TokenService tokenService;

	public JwtTokenAuthenticationManager(TokenService tokenService) {
		super();
		this.tokenService = tokenService;
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!this.supports(authentication.getClass())) {
			return null;
		}
		JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
		String jwtToken = (String) authenticationToken.getCredentials();
		try {
			Map<String, Object> claims = tokenService.parseClaimsToken(jwtToken);
			String subject = (String) claims.get(CommonConstant.JWT_SUBJECT_KEY);
			Long accId = Long.valueOf(claims.get(CommonConstant.JWT_ACCOUNT_ID_KEY).toString());
			Collection<GrantedAuthority> grantAuthors = Collections.singleton(new SimpleGrantedAuthority("USER_ROLE"));
			Object roleClaimObj = claims.get(CommonConstant.JWT_AUTHORZIED_KEY);
			if (roleClaimObj != null && Collection.class.isInstance(roleClaimObj)) {
				Collection<String> allRoles = (Collection<String>) roleClaimObj;
				grantAuthors = allRoles.parallelStream().map((role) -> new SimpleGrantedAuthority(role))
						.collect(Collectors.toSet());
			}
			JwtTokenPrincipal jwtTokenPrincipal = new JwtTokenPrincipal(subject, accId, grantAuthors);
			return new JwtAuthenticationToken(jwtTokenPrincipal, null, grantAuthors);
		} catch (ClassCastException ccEx) {
			throw new BadCredentialsException("Invalid authorzied.");
		} catch (Exception ex) {
			throw new BadCredentialsException("Invalid token.", ex);
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return JwtAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
