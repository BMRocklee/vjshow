package com.vjshow.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.WalletTransactionEntity;

public interface WalletTransactionRepository extends JpaRepository<WalletTransactionEntity, String> {
}
