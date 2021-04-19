package com.firebase.auth.example.service;

import com.firebase.auth.example.dto.request.RegisterAccountRequest;
import com.firebase.auth.example.entity.AccountEntity;

public interface AccountService {
	AccountEntity createAccount(RegisterAccountRequest accountRequest) throws Exception;
}
