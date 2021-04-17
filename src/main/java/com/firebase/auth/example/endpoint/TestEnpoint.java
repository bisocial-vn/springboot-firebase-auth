package com.firebase.auth.example.endpoint;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEnpoint {

	@GetMapping("/public/ping")
	public ResponseEntity<?> ping() {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("ping", "pong");
		return ResponseEntity.ok(body);
	}
}
