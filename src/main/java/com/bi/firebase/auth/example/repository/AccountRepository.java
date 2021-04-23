package com.bi.firebase.auth.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bi.firebase.auth.example.entity.AccountEntity;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

	Optional<AccountEntity> findByEmail(String email);

	Optional<AccountEntity> findByPhone(String phone);
}
