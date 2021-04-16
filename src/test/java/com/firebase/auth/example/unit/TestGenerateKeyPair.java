package com.firebase.auth.example.unit;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Comparator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestGenerateKeyPair {

	private static final boolean cleanupTempFolder = true;

	private static final String DEFAULT_ALGORITHM = "RSA";

	@Test
	public void testGenerateKeyPairBase64() throws NoSuchAlgorithmException {
		KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance(DEFAULT_ALGORITHM);
		rsaKeyPairGenerator.initialize(2048);
		KeyPair rsaKeyPair = rsaKeyPairGenerator.generateKeyPair();
		PublicKey rsaPublicKey = rsaKeyPair.getPublic();
		PrivateKey rsaPrivateKey = rsaKeyPair.getPrivate();
		log.info("RSA private key:\n" + Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()));
		log.info("RSA public key:\n" + Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()));
	}

	@Test
	public void testGenerateKeyPairFile() throws NoSuchAlgorithmException, IOException {
		KeyPairGenerator rsaKeyPairGenerator = KeyPairGenerator.getInstance(DEFAULT_ALGORITHM);
		rsaKeyPairGenerator.initialize(2048);
		KeyPair rsaKeyPair = rsaKeyPairGenerator.generateKeyPair();
		PublicKey rsaPublicKey = rsaKeyPair.getPublic();
		PrivateKey rsaPrivateKey = rsaKeyPair.getPrivate();

		String rsaPrivateKeyBase64 = Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded());
		log.info("RSA private key:\n" + rsaPrivateKeyBase64);
		String rsaPublicKeyBase64 = Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded());
		log.info("RSA public key:\n" + rsaPublicKeyBase64);
		Path tmpFolderPath = Files.createTempDirectory("tmp-key-pair");
		log.info("Temp folder for writing key pair: " + tmpFolderPath.toString());

		Path privateFilePath = Files.createTempFile(tmpFolderPath, "private", "");
		log.info("Private file path: " + privateFilePath.toString());
		Files.write(privateFilePath, rsaPrivateKey.getEncoded(), StandardOpenOption.WRITE);
		assertFalse(privateFilePath.toFile().length() == 0);
		log.info("Private file length: " + privateFilePath.toFile().length());
		byte[] privateKeyByteArr = Files.readAllBytes(privateFilePath);
		assertArrayEquals(privateKeyByteArr, rsaPrivateKey.getEncoded());
		log.info("Private file base64 encode:\n" + Base64.getEncoder().encodeToString(privateKeyByteArr));

		Path publicFilePath = Files.createTempFile(tmpFolderPath, "public", "");
		log.info("Public file path: " + publicFilePath.toString());
		Files.write(publicFilePath, rsaPublicKey.getEncoded(), StandardOpenOption.WRITE);
		assertFalse(publicFilePath.toFile().length() == 0);
		log.info("Public file length: " + publicFilePath.toFile().length());
		byte[] publicKeyByteArr = Files.readAllBytes(publicFilePath);
		assertArrayEquals(publicKeyByteArr, rsaPublicKey.getEncoded());
		log.info("Public file base64 encode:\n" + Base64.getEncoder().encodeToString(publicKeyByteArr));

		if (cleanupTempFolder) {
			Files.walk(tmpFolderPath).sorted(Comparator.reverseOrder()).map(Path::toFile).peek(System.out::println)
					.forEach(File::delete);
			assertFalse(Files.exists(tmpFolderPath));

		}
	}

	@Test
	@DisplayName("testGenerateKeyPairForJwtToken")
	public void testGenerateKeyPairForJwtToken() {
		KeyPair rsa256KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
		PublicKey rsaPublicKey = rsa256KeyPair.getPublic();
		PrivateKey rsaPrivateKey = rsa256KeyPair.getPrivate();
		log.info("{}. RSA private key: \n{}\n", SignatureAlgorithm.RS256,
				Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()));
		log.info("{}. RSA public key:\n{}\n", SignatureAlgorithm.RS256,
				Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()));
	}

}
