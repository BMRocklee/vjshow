package com.vjshow.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.entity.UserEntity;

public interface OrderService {
	PaymentEntity createNewOrder(Long productId, UserEntity buyer);
	
	Optional<OrderEntity> findPending(Long buyerId, Long productId);

    void markPaid(OrderEntity order);
    
    List<OrderEntity> getPaidOrders(Long userId);

    OrderEntity getOrderById(Long id);
}
