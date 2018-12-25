package com.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.demo.model.Account;

public interface AccountRepository extends JpaRepository<Account, Integer> {
	Optional<Account> findByEmail(String name);
}
