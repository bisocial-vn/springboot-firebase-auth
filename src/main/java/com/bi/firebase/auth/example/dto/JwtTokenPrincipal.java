package com.bi.firebase.auth.example.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenPrincipal {
	private String subject;
	private Long accId;
	private Collection<GrantedAuthority> roles;
}
