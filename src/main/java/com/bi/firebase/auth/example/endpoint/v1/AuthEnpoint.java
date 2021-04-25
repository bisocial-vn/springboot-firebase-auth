package com.bi.firebase.auth.example.endpoint.v1;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bi.firebase.auth.example.configuration.properties.JwtProperties;
import com.bi.firebase.auth.example.constant.CommonConstant;
import com.bi.firebase.auth.example.dto.JwtTokenPrincipal;
import com.bi.firebase.auth.example.dto.request.LoginRequest;
import com.bi.firebase.auth.example.dto.response.BaseApiErrorResponse;
import com.bi.firebase.auth.example.dto.response.TokenResponse;
import com.bi.firebase.auth.example.service.AuthService;
import com.bi.firebase.auth.example.utils.ValidationUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthEnpoint {

	@Autowired
	private AuthService authService;
	@Autowired
	private JwtProperties jwtProperties;

	@PostMapping("/public/auth/token")
	public ResponseEntity<?> loginWithCredential(@Valid @RequestBody LoginRequest loginRequest,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			ValidationUtils.getInstance().validateLoginName(loginRequest.getLoginname());
		} catch (IllegalArgumentException argumentEx) {
			BaseApiErrorResponse apiErrorResponse = new BaseApiErrorResponse("auth-error",
					"Email or Phone invalid format.", argumentEx.getMessage(), null);
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
		}
		try {
			TokenResponse tokenResponse = authService.authenticationWithCredential(loginRequest.getLoginname(),
					loginRequest.getPassword(), loginRequest.isRemember());
			if (loginRequest.isRemember()) {
				Cookie cookie = new Cookie(CommonConstant.DEFAULT_REFRESH_TOKEN_KEY, tokenResponse.getRefreshToken());
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				cookie.setSecure(request.isSecure());
				cookie.setMaxAge((int) jwtProperties.getRefreshTokenDuration().getSeconds());
				response.addCookie(cookie);
			}
			return ResponseEntity.ok(tokenResponse);
		} catch (RuntimeException authEx) {
			BaseApiErrorResponse apiErrorResponse = new BaseApiErrorResponse("auth-error", "Authentication failed.",
					authEx.getMessage(), null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
		}
	}

	@GetMapping("/public/auth/refresh")
	public ResponseEntity<?> loginWithRefreshToken(
			@CookieValue(name = CommonConstant.DEFAULT_REFRESH_TOKEN_KEY, defaultValue = "") String encryptedRefreshToken) {
		if (!StringUtils.hasText(encryptedRefreshToken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		try {
			TokenResponse tokenResponse = authService.authentiationWithRefreshToken(encryptedRefreshToken);
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/auth/info")
	public ResponseEntity<?> getAuthInfo() {
		Authentication authen = SecurityContextHolder.getContext().getAuthentication();
		if (authen == null || !JwtTokenPrincipal.class.isInstance(authen.getPrincipal())) {
			log.debug("Principal is not typeof JwtTokenPrincipal: {}", authen.getPrincipal());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		JwtTokenPrincipal jwtTokenPrincipal = (JwtTokenPrincipal) authen.getPrincipal();
		return ResponseEntity.ok(jwtTokenPrincipal);
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(
			@CookieValue(name = CommonConstant.DEFAULT_REFRESH_TOKEN_KEY, defaultValue = "") String encryptedRefreshToken,
			HttpServletResponse response) {
		Cookie refreshTokenCookie = new Cookie(CommonConstant.DEFAULT_REFRESH_TOKEN_KEY, null);
		refreshTokenCookie.setMaxAge(0);
		response.addCookie(refreshTokenCookie);
		if (!StringUtils.hasText(encryptedRefreshToken)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		try {
			authService.deleteRefreshToken(encryptedRefreshToken);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/logout/all")
	public ResponseEntity<?> logoutAll() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !JwtTokenPrincipal.class.isInstance(authentication.getPrincipal())) {
			log.debug("Can not get authentication principal: {}", authentication);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		JwtTokenPrincipal jwtTokenPrincipal = (JwtTokenPrincipal) authentication.getPrincipal();
		Long accId = Long.valueOf(jwtTokenPrincipal.getAccId());
		authService.deleteAllAccountRefreshToken(accId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
