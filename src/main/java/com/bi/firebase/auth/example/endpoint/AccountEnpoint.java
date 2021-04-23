package com.bi.firebase.auth.example.endpoint;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bi.firebase.auth.example.dto.request.RegisterAccountWithEmailRequest;
import com.bi.firebase.auth.example.dto.request.RegisterAccountWithPhoneRequest;

@RestController
public class AccountEnpoint {

	@PostMapping("/public/account/phone")
	public ResponseEntity<?> registerAccountWithPhone(@Valid @RequestBody RegisterAccountWithPhoneRequest emailReq) {

		return null;
	}

	@PostMapping("/public/account/email")
	public ResponseEntity<?> registerAccountWithEmail(@Valid @RequestBody RegisterAccountWithEmailRequest emailReq) {

		return null;
	}
}
