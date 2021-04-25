package com.bi.firebase.auth.example.endpoint.v1;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bi.firebase.auth.example.configuration.properties.JwtProperties;
import com.bi.firebase.auth.example.constant.CommonConstant;
import com.bi.firebase.auth.example.dto.ProviderId;
import com.bi.firebase.auth.example.dto.request.FirebaseRegisterAccountRequest;
import com.bi.firebase.auth.example.dto.response.BaseApiErrorResponse;
import com.bi.firebase.auth.example.dto.response.TokenResponse;
import com.bi.firebase.auth.example.service.AccountService;

@RestController
public class AccountEnpoint {

	@Autowired
	private AccountService accountService;
	@Autowired
	private JwtProperties jwtProperties;

	@PostMapping("/public/account/{providerId}")
	public ResponseEntity<?> registerAccountWithPhone(
			@Valid @RequestBody FirebaseRegisterAccountRequest firebaseRegisterAccountRequest,
			@PathVariable(name = "providerId", required = false) String providerId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TokenResponse tokenResponse = accountService.createAccount(
					new FirebaseRegisterAccountRequest(firebaseRegisterAccountRequest.getIdToken(),
							firebaseRegisterAccountRequest.getPassword()),
					ProviderId.valueOf(providerId.toUpperCase()));
			if (StringUtils.hasText(tokenResponse.getRefreshToken())) {
				Cookie cookie = new Cookie(CommonConstant.DEFAULT_REFRESH_TOKEN_KEY, tokenResponse.getRefreshToken());
				cookie.setPath("/");
				cookie.setHttpOnly(true);
				cookie.setSecure(request.isSecure());
				cookie.setMaxAge((int) jwtProperties.getRefreshTokenDuration().getSeconds());
				response.addCookie(cookie);
			}
			return ResponseEntity.ok().body(tokenResponse);
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(BaseApiErrorResponse.builder().title(e.getMessage()).details(e.getMessage()).build());
		}
	}

	@GetMapping("/public/account/validation/email")
	public ResponseEntity<?> isValidEmail(@RequestParam(name = "email", required = true) String email) {
		try {
			accountService.validateEmail(email);
		} catch (Exception e) {
			BaseApiErrorResponse errorResponse = BaseApiErrorResponse.builder().type("validation-error")
					.title("Email validation error.").details(e.getMessage())
					.instance("/public/account/validation/email").build();
			return ResponseEntity.badRequest().body(errorResponse);
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/public/account/validation/phone")
	public ResponseEntity<?> isValidPhone(@RequestParam(name = "phone", required = true) String phone) {
		try {
			accountService.validatePhone(phone);
		} catch (Exception e) {
			BaseApiErrorResponse errorResponse = BaseApiErrorResponse.builder().type("validation-error")
					.title("Phone validation error.").details(e.getMessage())
					.instance("/public/account/validation/phone").build();
			return ResponseEntity.badRequest().body(errorResponse);
		}
		return ResponseEntity.ok().build();
	}
}
