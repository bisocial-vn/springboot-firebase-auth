package com.firebase.auth.example.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class RegisterAccountRequest {
	@NotBlank(message = "Token is required.")
	private String idToken;
	@NotBlank(message = "Password is required.")
	private String password;
}
