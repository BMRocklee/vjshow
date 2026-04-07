package com.vjshow.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.WithdrawRequestEntity;

public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequestEntity, String> {
}