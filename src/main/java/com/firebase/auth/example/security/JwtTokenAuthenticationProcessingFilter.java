package com.firebase.auth.example.security;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import com.firebase.auth.example.configuration.properties.JwtProperties;
import com.firebase.auth.example.constant.CommonConstant;
import com.firebase.auth.example.dto.JwtTokenPrincipal;
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
		String jwtToken = this.getJwtTokenFromRequest(request);
		if (!StringUtils.hasText(jwtToken)) {
			throw new BadCredentialsException("Invalid token.");
		}
		try {
			Map<String, Object> claims = tokenService.parseClaimsToken(jwtToken);
			Long subject = (Long) claims.get(CommonConstant.JWT_SUBJECT_KEY);
			Set<String> roles = (Set<String>) claims.getOrDefault(CommonConstant.JWT_AUTHORZIED_KEY,
					Collections.singleton("ROLE_USER"));
			Set<GrantedAuthority> authors = roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
			JwtTokenPrincipal jwtTokenPrincipal = new JwtTokenPrincipal(String.valueOf(subject), authors);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(jwtTokenPrincipal, null,
					authors);
			return token;
		} catch (ClassCastException ccEx) {
			throw new BadCredentialsException("Invalid authorzied.");
		} catch (Exception ex) {
			throw new BadCredentialsException("Invalid token.", ex);
		}
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
