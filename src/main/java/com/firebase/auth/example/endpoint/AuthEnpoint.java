package com.firebase.auth.example.endpoint;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.firebase.auth.example.configuration.properties.JwtProperties;
import com.firebase.auth.example.constant.CommonConstant;
import com.firebase.auth.example.dto.request.LoginRequest;
import com.firebase.auth.example.dto.response.BaseApiErrorResponse;
import com.firebase.auth.example.dto.response.TokenResponse;
import com.firebase.auth.example.service.AuthService;
import com.firebase.auth.example.utils.ValidationUtils;

@RestController
public class AuthEnpoint {

	@Autowired
	private AuthService authService;
	@Autowired
	private JwtProperties jwtProperties;

	@PostMapping("/public/auth/token")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
		try {
			ValidationUtils.getInstance().validateLoginName(loginRequest.getLoginname());
		} catch (IllegalArgumentException argumentEx) {
			BaseApiErrorResponse apiErrorResponse = new BaseApiErrorResponse("auth-error",
					"Email or Phone invalid format.", argumentEx.getMessage(), null);
			ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
		}
		try {
			TokenResponse tokenResponse = authService.authenticationUser(loginRequest.getLoginname(),
					loginRequest.getPassword(), loginRequest.isRemember());
			if (loginRequest.isRemember()) {
				Cookie cookie = new Cookie(CommonConstant.DEFAULT_REFRESH_TOKEN_KEY, tokenResponse.getRefreshToken());
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				cookie.setMaxAge((int) jwtProperties.getRefreshTokenDuration().getSeconds());
			}
			return ResponseEntity.ok(tokenResponse);
		} catch (RuntimeException authEx) {
			BaseApiErrorResponse apiErrorResponse = new BaseApiErrorResponse("auth-error", "Authentication failed.",
					authEx.getMessage(), null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
		}
	}
}
