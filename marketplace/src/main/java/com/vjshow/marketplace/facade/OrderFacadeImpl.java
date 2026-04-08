package com.vjshow.marketplace.facade;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.dto.response.CreatePaymentResponse;
import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.entity.UserEntity;
import com.vjshow.marketplace.service.OrderService;
import com.vjshow.marketplace.service.PaymentGatewayService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {

	private final OrderService orderService;

	private final PaymentGatewayService paymentGateway;

	@Override
	public CreatePaymentResponse createOrder(Long productId, UserEntity buyer) {
		// 1. create payment + order
		PaymentEntity payment = orderService.createNewOrder(productId, buyer);

		// 2. generate QR
//		String qr = paymentGateway.generateCassoQr(payment);
		String qr = paymentGateway.generatePayOsQR(payment);

		// 3. response
		return new CreatePaymentResponse(payment.getId(), qr, payment.getAmount());
	}

}
