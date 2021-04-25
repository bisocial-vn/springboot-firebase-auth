package com.bi.firebase.auth.example.service;

import com.bi.firebase.auth.example.dto.ProviderId;
import com.bi.firebase.auth.example.dto.request.FirebaseRegisterAccountRequest;
import com.bi.firebase.auth.example.dto.response.TokenResponse;

public interface AccountService {
	TokenResponse createAccount(FirebaseRegisterAccountRequest accountRequest, ProviderId providerId) throws Exception;

	void validateEmail(String email) throws Exception;

	void validatePhone(String phone) throws Exception;
}
