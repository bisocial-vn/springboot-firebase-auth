package com.firebase.auth.example.service;

public interface CipherService {

	byte[] encrypt(final byte[] data);

	byte[] decrypt(final byte[] data);

	String encryptText(final String data);

	String decryptText(final String data);

}
