package com.firebase.auth.example.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import com.firebase.auth.example.configuration.properties.JwtProperties;
import com.firebase.auth.example.constant.CommonConstant;
import com.firebase.auth.example.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

	private JwtProperties jwtProperties;

	@Autowired
	TokenService tokenService;

	public JwtTokenAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher,
			JwtProperties jwtProperties) {
		super(requiresAuthenticationRequestMatcher == null ? CommonConstant.PROTECTED_URLS
				: requiresAuthenticationRequestMatcher);
		this.jwtProperties = jwtProperties;
		this.setContinueChainBeforeSuccessfulAuthentication(false);
	}

//	public JwtTokenAuthenticationProcessingFilter(RequestMatcher requiresAuthenticationRequestMatcher,
//			AuthenticationManager authenticationManager) {
//		super(requiresAuthenticationRequestMatcher, authenticationManager);
//	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		log.debug("Attemple JwtTokenAuthenticationFilter:AbstractAuthenticationProcessingFilter - {}",
				request.getRequestURI());
		log.info("{} - {}", "JwtTokenAuthenticationProcessingFilter", request.getRequestURL());
		String jwtToken = this.getJwtTokenFromRequest(request);
		if (!StringUtils.hasText(jwtToken)) {
			throw new BadCredentialsException("Invalid token.");
		}
		JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(jwtToken);
		return this.getAuthenticationManager().authenticate(jwtAuthenticationToken);
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

	private String getJwtTokenFromRequest(HttpServletRequest request) {
		final String bearerHeader = request.getHeader(jwtProperties.getHeader());
		if (StringUtils.hasText(bearerHeader) && bearerHeader.startsWith(jwtProperties.getType())) {
			return bearerHeader.substring(jwtProperties.getType().length()).trim();
		}
		return null;
	}

}
