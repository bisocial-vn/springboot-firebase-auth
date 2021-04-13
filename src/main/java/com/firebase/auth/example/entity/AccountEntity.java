package com.firebase.auth.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity(name = "Account")
@Table(name = "ACCOUNT")
@Data
public class AccountEntity {

	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true, name = "EMAIL")
	private String email;
	@Column(unique = true, name = "PHONE")
	private String phone;
	@Column(name = "PASSWORD", nullable = false)
	private String password;
}
