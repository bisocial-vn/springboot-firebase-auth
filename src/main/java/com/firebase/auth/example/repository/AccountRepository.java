package com.firebase.auth.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.firebase.auth.example.entity.AccountEntity;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

	Optional<AccountEntity> findOneByEmail(String email);

	Optional<AccountEntity> findOneByPhone(String phone);
}
