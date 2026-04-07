package com.vjshow.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.WithdrawRequestEntity;

public interface WithdrawRequestRepository extends JpaRepository<WithdrawRequestEntity, String> {
	List<WithdrawRequestEntity> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);
}