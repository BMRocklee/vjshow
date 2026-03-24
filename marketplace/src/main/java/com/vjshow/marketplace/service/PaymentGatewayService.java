package com.vjshow.marketplace.service;

import com.vjshow.marketplace.entity.PaymentEntity;

public interface PaymentGatewayService {
	String generateQr(PaymentEntity payment);
}
