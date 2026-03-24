package com.vjshow.marketplace.facade;

import com.vjshow.marketplace.dto.response.CreatePaymentResponse;
import com.vjshow.marketplace.entity.UserEntity;

public interface OrderFacade {
	CreatePaymentResponse createOrder(Long productId, UserEntity buyer);
}
