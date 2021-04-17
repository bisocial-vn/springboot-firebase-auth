package com.firebase.auth.example.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterAccountWithPhoneRequest {
	@NotBlank(message = "Token is required.")
	private String idToken;
	@NotBlank(message = "Password is required.")
	private String password;
}
