package com.vjshow.marketplace.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.vjshow.marketplace.entity.PaymentEntity;

import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;

@Service
public class VietQrGatewayImpl implements PaymentGatewayService {

	@Value("${payos.client-id}")
	private String clientId;

	@Value("${payos.api-key}")
	private String apiKey;

	@Value("${payos.checksum-key}")
	private String checkSumKey;

	@Override
	public String generateCassoQr(PaymentEntity payment) {
		return "https://img.vietqr.io/image/OCB-0949892138-compact.png" + "?amount=" + payment.getAmount() + "&addInfo="
				+ payment.getContent() + "&accountName=VJSHOW";
	}

	@Override
	public String generatePayOsQR(PaymentEntity payment) {
		
	    final PayOS payOS = new PayOS(clientId, apiKey, checkSumKey);
	    
	    Long orderCode = System.currentTimeMillis() / 1000;
        CreatePaymentLinkRequest paymentData =
            CreatePaymentLinkRequest.builder()
                .orderCode(orderCode)
                .amount(payment.getAmount())
                .description(payment.getContent())
                .returnUrl("https://vjshow.vn/payment-result")
                .cancelUrl("https://vjshow.vn/payment-result")
                .build();
        
        CreatePaymentLinkResponse result = payOS.paymentRequests().create(paymentData);
        return result.getCheckoutUrl();
	}
}
