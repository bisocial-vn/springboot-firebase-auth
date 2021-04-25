package com.bi.firebase.auth.example.dto.request;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirebaseRegisterAccountRequest {
	@NotBlank(message = "Token is required.")
	private String idToken;
	@NotBlank(message = "Password is required.")
	@Length(min = 6, message = "Password must at least 6 character long.")
	private String password;
}
