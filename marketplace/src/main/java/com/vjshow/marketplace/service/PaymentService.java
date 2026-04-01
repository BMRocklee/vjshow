package com.vjshow.marketplace.service;

import java.util.Optional;

import com.vjshow.marketplace.dto.response.PaymentStatusResponse;
import com.vjshow.marketplace.entity.PaymentEntity;

public interface PaymentService {
//	PaymentEntity createPayment(Long amount);
	
    void markSuccess(PaymentEntity payment);

    Optional<PaymentEntity> findByContent(String content);
    
    PaymentStatusResponse checkPayment(String paymentId, Long userId);
}
