package com.bi.firebase.auth.example.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.bi.firebase.auth.example.entity.AuthTokenEntity;

public interface AuthTokenRepository extends JpaRepository<AuthTokenEntity, Long> {
	Optional<AuthTokenEntity> findByRefreshToken(String refreshToken);

	@Modifying
	@Query("delete from AuthToken at where at.refreshToken = :refreshToken")
	int deleteByRefreshToken(String refreshToken);

	@Modifying
	@Query("delete from AuthToken at where at.account.id = :accountId")
	int deleteByAccountId(Long accountId);

	@Modifying
	@Query("delete from AuthToken at where at.expiryDate < :beforeDate")
	void cleanupFromDate(Date beforeDate);
}
