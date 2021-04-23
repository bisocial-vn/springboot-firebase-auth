package com.bi.firebase.auth.example.service;

import com.bi.firebase.auth.example.dto.request.RegisterAccountRequest;
import com.bi.firebase.auth.example.entity.AccountEntity;

public interface AccountService {
	AccountEntity createAccount(RegisterAccountRequest accountRequest) throws Exception;
}
