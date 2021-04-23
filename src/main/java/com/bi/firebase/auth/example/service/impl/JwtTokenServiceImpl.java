package com.bi.firebase.auth.example.service.impl;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.bi.firebase.auth.example.configuration.properties.JwtProperties;
import com.bi.firebase.auth.example.constant.CommonConstant;
import com.bi.firebase.auth.example.core.jwt.JwtTokenIssuer;
import com.bi.firebase.auth.example.core.jwt.JwtTokenVerify;
import com.bi.firebase.auth.example.service.TokenService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtTokenServiceImpl implements TokenService {

	private JwtProperties jwtProperties;
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public JwtTokenServiceImpl(JwtProperties jwtProperties) throws Exception {
		this.jwtProperties = jwtProperties;
		try {
			Resource resource = new UrlResource(jwtProperties.getPrivatekeyPath());
			byte[] privateKeyRaw = StreamUtils.copyToByteArray(resource.getInputStream());
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyRaw);
			this.privateKey = KeyFactory.getInstance(CommonConstant.DEFAULT_RSA_ALGORITHM)
					.generatePrivate(privateKeySpec);
			byte[] publicKeyRaw = Base64.getDecoder().decode(jwtProperties.getPublickey());
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyRaw);
			this.publicKey = KeyFactory.getInstance(CommonConstant.DEFAULT_RSA_ALGORITHM).generatePublic(publicKeySpec);
		} catch (Exception ex) {
			log.error("Initial JWT token service fail.", ex);
			log.debug("PrivateKey path: {}", jwtProperties.getPrivatekeyPath());
			log.debug("PublicKey: {}", jwtProperties.getPublickey());
			throw new Exception("Initial JWT Token Service fail.");
		}
	}

	@Override
	public String generateToken(String subject, Map<String, Object> claims) {
		return JwtTokenIssuer.getInstance().issuanceToken(privateKey, subject,
				jwtProperties.getAccessTokenDuration().toMillis(), claims);
	}

	@Override
	public Map<String, Object> parseClaimsToken(String jwtToken) throws Exception {
		return JwtTokenVerify.getInstance().verifyToken(publicKey, jwtToken);
	}

}
