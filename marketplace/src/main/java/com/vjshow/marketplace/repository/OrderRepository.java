package com.vjshow.marketplace.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.enums.OrderStatusEnum;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
	Optional<OrderEntity> findTopByBuyerIdAndProductIdOrderByCreatedAtDesc(Long buyerId, Long productId);

	Optional<OrderEntity> findByPaymentId(String id);
	
	 boolean existsByProduct_Id(Long productId);

	List<OrderEntity> findByBuyerIdAndStatus(Long userId, OrderStatusEnum paid);
}
