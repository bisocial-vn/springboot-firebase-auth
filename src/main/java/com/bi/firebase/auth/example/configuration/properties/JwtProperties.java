package com.bi.firebase.auth.example.configuration.properties;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties("app.jwt")
public class JwtProperties {

	private Duration accessTokenDuration = Duration.ofDays(1l);
	private Duration refreshTokenDuration = Duration.ofDays(30l);
	private String publickey;
	private String privatekeyPath;
	private String header;
	private String type;
	private String refreshTokenKey;
	private String refreshTokenSalt;

}
