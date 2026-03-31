package com.vjshow.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {	
	Optional<PaymentEntity> findByContent(String content);
}
