package com.firebase.auth.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseApiErrorResponse {
	private String type;
	private String title;
	private String details;
	private String instance;
}
