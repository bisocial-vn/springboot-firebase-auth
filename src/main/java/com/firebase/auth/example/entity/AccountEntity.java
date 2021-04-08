package com.firebase.auth.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "Account")
@Table(name = "ACCOUNT")
@Data
@EqualsAndHashCode(callSuper = true)
public class AccountEntity extends BasicEntityAudit {

	@Column(unique = true, name = "EMAIL")
	private String email;
	@Column(unique = true, name = "PHONE")
	private String phone;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
}
