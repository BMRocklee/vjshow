package com.vjshow.marketplace.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vjshow.marketplace.dto.request.CassoWebhookDto;
import com.vjshow.marketplace.dto.request.CassoWebhookDto.CassoTransaction;
import com.vjshow.marketplace.dto.response.PayOSWebhookDto;
import com.vjshow.marketplace.entity.OrderEntity;
import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.enums.PaymentStatusEnum;
import com.vjshow.marketplace.repository.OrderRepository;
import com.vjshow.marketplace.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {

	private final PaymentService paymentService;

	private final OrderService orderService;

	private final WalletService walletService;

	private final OrderRepository orderRepo;

	private final ProductRepository productRepo;

	@Override
	@Transactional
	public void handleCasso(CassoWebhookDto body) {

		for (CassoTransaction tx : body.getData()) {

			String content = tx.getDescription();

			if (content == null || !content.startsWith("VJSHOW"))
				continue;

			PaymentEntity payment = paymentService.findByContent(content).orElse(null);

			if (payment == null)
				continue;

			// idempotent
			if (payment.getStatus() == PaymentStatusEnum.SUCCESS)
				continue;

			if (!payment.getAmount().equals(tx.getAmount()))
				continue;

			// ✅ update payment
			payment.setStatus(PaymentStatusEnum.PAID);
			payment.setPaidAt(LocalDateTime.now());
			paymentService.markSuccess(payment);

			// ✅ update order
			OrderEntity order = orderRepo.findByPaymentId(payment.getId()).orElseThrow();

			// ✅ update product
			productRepo.incrementSales(order.getProduct().getId());

			walletService.handleOrderPaid(order);

			orderService.markPaid(order);
		}
	}

	@Override
	@Transactional
	public void handlePayOS(PayOSWebhookDto body) {

		// ✅ 1. Check webhook hợp lệ
		if (body == null || body.getSuccess() == null || !body.getSuccess() || !"00".equals(body.getCode())) {
			return;
		}

		if (body.getData() == null)
			return;

		var data = body.getData();

		// ✅ 2. Check giao dịch thành công
		if (!"00".equals(data.getCode())) {
			return;
		}

		String content = data.getDescription();

		if (content == null || !content.startsWith("VJSHOW")) {
			return;
		}

		// ✅ 3. Tìm payment theo content (giống Casso)
		PaymentEntity payment = paymentService.findByContent(content).orElse(null);

		if (payment == null)
			return;

		// ✅ 4. Idempotent (tránh xử lý 2 lần)
		if (payment.getStatus() == PaymentStatusEnum.SUCCESS) {
			return;
		}

		// ✅ 5. Check amount (chống fake)
		if (!payment.getAmount().equals(data.getAmount())) {
			return;
		}

		// ✅ 6. Update payment
		payment.setStatus(PaymentStatusEnum.PAID);
		payment.setPaidAt(LocalDateTime.now());
		paymentService.markSuccess(payment);

		// ✅ 7. Update order
		OrderEntity order = orderRepo.findByPaymentId(payment.getId()).orElseThrow();

		// ✅ 8. Update product
		productRepo.incrementSales(order.getProduct().getId());

		// ✅ 9. Update wallet
		walletService.handleOrderPaid(order);

		// ✅ 10. Mark order paid
		orderService.markPaid(order);
	}
}
