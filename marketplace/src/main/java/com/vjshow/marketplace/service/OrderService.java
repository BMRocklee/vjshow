package com.vjshow.marketplace.service;

import java.util.Optional;

import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.entity.UserEntity;

public interface OrderService {
	PaymentEntity createNewOrder(Long productId, UserEntity buyer);
	
	Optional<OrderEntity> findPending(Long buyerId, Long productId);

    void markPaid(OrderEntity order);
}
