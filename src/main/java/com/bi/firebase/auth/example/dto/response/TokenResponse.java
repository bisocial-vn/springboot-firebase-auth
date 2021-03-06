package com.bi.firebase.auth.example.dto.response;

import java.util.Date;

import lombok.Data;

@Data
public class TokenResponse {
	private String accessToken;
	private String refreshToken;
	private String type;
	private Date accessTokenExpiryDate;
	private Date refreshTokenExpiryDate;
}
