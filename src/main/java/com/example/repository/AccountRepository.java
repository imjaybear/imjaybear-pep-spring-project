package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{
    Account findByUsername(String username);

    static Optional<Account> findById(Integer posted_by) {
        return null;
    }

}
