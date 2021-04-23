package com.bi.firebase.auth.example.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.bi.firebase.auth.example.entity.AccountEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class AccountDetails implements UserDetails {

	private static final long serialVersionUID = 238209870451420114L;

	private AccountEntity accountEntity;

	public AccountDetails(AccountEntity accountEntity) {
		this.accountEntity = accountEntity;
	}

	public Long getAccountId() {
		return accountEntity.getId();
	}

	public String getEmail() {
		return accountEntity.getEmail();
	}

	public String getPhone() {
		return this.accountEntity.getPhone();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	@JsonIgnoreProperties
	public String getPassword() {
		return accountEntity.getPassword();
	}

	@Override
	public String getUsername() {
		return this.getEmail() == null ? this.getPhone() : this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
