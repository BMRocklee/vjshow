package com.vjshow.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.CreatorWalletEntity;

public interface CreatorWalletRepository extends JpaRepository<CreatorWalletEntity, Long> {
	
}
