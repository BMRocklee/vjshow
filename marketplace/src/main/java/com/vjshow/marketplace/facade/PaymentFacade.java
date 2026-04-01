package com.vjshow.marketplace.facade;

import org.springframework.security.core.Authentication;

import com.vjshow.marketplace.dto.response.PaymentStatusResponse;

public interface PaymentFacade {
	PaymentStatusResponse check(Authentication authentication, String paymentId);
}
