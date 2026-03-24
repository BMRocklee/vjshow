package com.vjshow.marketplace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.OrderEntity;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
	Optional<OrderEntity> findTopByBuyerIdAndProductIdOrderByCreatedAtDesc(Long buyerId, Long productId);

	Optional<OrderEntity> findByPaymentId(String id);
}
