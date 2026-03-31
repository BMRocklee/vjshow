package com.vjshow.marketplace.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vjshow.marketplace.dto.request.CassoWebhookDto;
import com.vjshow.marketplace.dto.request.CassoWebhookDto.CassoTransaction;
import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.enums.PaymentStatusEnum;
import com.vjshow.marketplace.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {
	
	private final PaymentService paymentService;
	
	private final OrderService orderService;
	
	private final OrderRepository orderRepo;
		
	@Override
    @Transactional
    public void handleCasso(CassoWebhookDto body) {

        for (CassoTransaction tx : body.getData()) {

            String content = tx.getDescription();

            if (content == null || !content.startsWith("VJSHOW-")) continue;

            PaymentEntity payment = paymentService
                .findByContent(content)
                .orElse(null);

            if (payment == null) continue;

            // idempotent
            if (payment.getStatus() == PaymentStatusEnum.SUCCESS) continue;

            if (!payment.getAmount().equals(tx.getAmount())) continue;

            // ✅ update payment
            paymentService.markSuccess(payment);

            // ✅ update order
            OrderEntity order = orderRepo.findByPaymentId(payment.getId())
                .orElseThrow();

            orderService.markPaid(order);
        }
    }
}
