package com.firebase.auth.example.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.firebase.auth.example.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public JwtTokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
		super(requiresAuthenticationRequestMatcher == null ? CommonConstant.PROTECTED_URLS
				: requiresAuthenticationRequestMatcher);
		this.setContinueChainBeforeSuccessfulAuthentication(false);
	}

	public JwtTokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
			AuthenticationManager authenticationManager) {
		super(requiresAuthenticationRequestMatcher, authenticationManager);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.info("Attemple JwtTokenAuthenticationFilter:AbstractAuthenticationProcessingFilter - {}",
				request.getRequestURI());
		Set<GrantedAuthority> authozied = new HashSet<GrantedAuthority>();
		authozied.add(new SimpleGrantedAuthority("ROLE_USER"));
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken("AUTH", "AUTH", authozied);
		return token;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.successfulAuthentication(request, response, chain, authResult);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.unsuccessfulAuthentication(request, response, failed);
		response.sendError(HttpStatus.SC_UNAUTHORIZED, "Invalid token. Unauthentication.");
	}

}
