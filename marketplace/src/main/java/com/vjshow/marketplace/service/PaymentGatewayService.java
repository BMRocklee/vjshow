package com.vjshow.marketplace.service;

import com.vjshow.marketplace.entity.PaymentEntity;

public interface PaymentGatewayService {
	public String generateCassoQr(PaymentEntity payment);
	
	public String generatePayOsQR(PaymentEntity payment);
}
