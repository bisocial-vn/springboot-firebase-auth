package com.bi.firebase.auth.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity(name = "Account")
@Table(name = "accounts")
@Data
public class AccountEntity {

	@Id
	@GeneratedValue
	private Long id;

	@Column(unique = true, name = "email")
	private String email;
	@Column(unique = true, name = "phone")
	private String phone;
	@Column(name = "password", nullable = false)
	private String password;
	private String firebaseUid;
}
