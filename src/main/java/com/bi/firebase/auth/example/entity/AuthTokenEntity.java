package com.bi.firebase.auth.example.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "acc_id")
	private AccountEntity account;
}
