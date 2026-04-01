package com.vjshow.marketplace.facade;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.vjshow.marketplace.dto.response.PaymentStatusResponse;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentFacadeImpl implements PaymentFacade{
	
	private final PaymentService paymentService;
	
	 @Override
	    public PaymentStatusResponse check(Authentication authentication, String paymentId) {
		 
		 UserEntity currentUser = (UserEntity) authentication.getPrincipal();
	        return paymentService.checkPayment(paymentId, currentUser.getId());
	    }
}
