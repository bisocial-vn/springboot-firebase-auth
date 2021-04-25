package com.bi.firebase.auth.example.endpoint.v1;

import java.util.Date;
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
		body.put("date", new Date());
		return ResponseEntity.ok(body);
	}
}
