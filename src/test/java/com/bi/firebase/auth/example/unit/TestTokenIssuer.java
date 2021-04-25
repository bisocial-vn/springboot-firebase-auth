package com.bi.firebase.auth.example.unit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.bi.firebase.auth.example.core.jwt.JwtTokenIssuer;

import io.jsonwebtoken.io.Encoders;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestTokenIssuer {

	private static final String RSA_ALGORITHM = "RSA";

	@Test
	public void testIssueTokenWithRSA256PrivateKey() throws Exception {
		log.info("test Issue Token With RSA256 bit PrivateKey");
		PrivateKey privateKey = this.generatePrivateKeyWithSize(2048);
		String token = JwtTokenIssuer.getInstance().issueToken(privateKey, UUID.randomUUID().toString(),
				Duration.ofDays(1l).toMillis(), null);
		log.info("Token: {}", token);
		assertNotNull(token);
	}

	@Test
	public void testIssueTokenWithRSA384PrivateKey() throws Exception {
		log.info("test Issue Token With RSA384 bit PrivateKey");
		PrivateKey privateKey = this.generatePrivateKeyWithSize(3072);
		String token = JwtTokenIssuer.getInstance().issueToken(privateKey, UUID.randomUUID().toString(),
				Duration.ofDays(1l).toMillis(), null);
		log.info("Token: {}", token);
		assertNotNull(token);
	}

	@Test
	public void testIssueTokenWithRSA512PrivateKey() throws Exception {
		log.info("test Issue Token With RSA512 PrivateKey");
		PrivateKey privateKey = this.generatePrivateKeyWithSize(4096);
		String token = JwtTokenIssuer.getInstance().issueToken(privateKey, UUID.randomUUID().toString(),
				Duration.ofDays(1l).toMillis(), null);
		log.info("Token: {}", token);
		assertNotNull(token);
	}

	@Test
	public void testInvalidPrivateKey() {
		Throwable exception = assertThrows(RuntimeException.class, () -> {
			JwtTokenIssuer.getInstance().issueToken(null, UUID.randomUUID().toString(), 0, null);
		});
		log.info("testInvalidPrivateKey: {}", exception.getMessage());
	}

	@Test
	public void testInvalidPrivateKeyLength() throws NoSuchAlgorithmException {
		PrivateKey privateKey = this.generatePrivateKeyWithSize(2047);
		Throwable exception = assertThrows(RuntimeException.class, () -> {
			JwtTokenIssuer.getInstance().issueToken(privateKey, UUID.randomUUID().toString(), 0, null);
		});
		log.info("testInvalidPrivateKeyLength: {}", exception.getMessage());
	}

	private PrivateKey generatePrivateKeyWithSize(int size) throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
		keyPairGenerator.initialize(size);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		String base64PublicKey = Encoders.BASE64.encode(keyPair.getPublic().getEncoded());
		log.info("PublicKey[{}] base64 format: {}", size, base64PublicKey);
		return keyPair.getPrivate();
	}

}
