package com.vjshow.marketplace.service;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.entity.PaymentEntity;

@Service
public class VietQrGatewayImpl implements PaymentGatewayService {
	 @Override
	    public String generateQr(PaymentEntity payment) {
	        return "https://img.vietqr.io/image/OCB-0949892138-compact.png"
	                + "?amount=" + payment.getAmount()
	                + "&addInfo=" + payment.getContent()
	                + "&accountName=VJSHOW";
	    }
}
