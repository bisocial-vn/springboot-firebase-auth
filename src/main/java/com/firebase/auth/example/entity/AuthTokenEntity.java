package com.firebase.auth.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity(name = "AuthToken")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenEntity extends BasicEntityAudit {
	private String refreshToken;
	private Date expiryDate;
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "account_id", nullable = false)
	private AccountEntity account;
}
