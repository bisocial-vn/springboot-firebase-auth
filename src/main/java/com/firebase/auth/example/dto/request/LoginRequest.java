package com.firebase.auth.example.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	@NotBlank(message = "Email or Phone is required.")
	private String loginname;
	@NotBlank(message = "Password is required.")
	private String password;
	private boolean isRemember = false;
}
