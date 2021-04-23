package com.bi.firebase.auth.example.service.impl;

import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

import com.bi.firebase.auth.example.configuration.properties.JwtProperties;
import com.bi.firebase.auth.example.service.CipherService;

@Service
public class SpringCipherServiceImpl implements CipherService {

	private TextEncryptor textEncryptor;
	private BytesEncryptor bytesEncryptor;

	public SpringCipherServiceImpl(final JwtProperties jwtProperties) {
		this.textEncryptor = Encryptors.text(jwtProperties.getRefreshTokenKey(), jwtProperties.getRefreshTokenSalt());
		this.bytesEncryptor = Encryptors.stronger(jwtProperties.getRefreshTokenKey(),
				jwtProperties.getRefreshTokenSalt());
	}

	@Override
	public byte[] encrypt(final byte[] data) {
		return bytesEncryptor.encrypt(data);
	}

	@Override
	public byte[] decrypt(final byte[] data) {
		return bytesEncryptor.decrypt(data);
	}

	@Override
	public String encryptText(final String data) {
		return textEncryptor.encrypt(data);
	}

	@Override
	public String decryptText(final String data) {
		return textEncryptor.decrypt(data);
	}

}
