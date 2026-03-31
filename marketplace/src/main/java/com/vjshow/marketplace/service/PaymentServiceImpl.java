package com.vjshow.marketplace.service;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vjshow.marketplace.entity.PaymentEntity;
import com.vjshow.marketplace.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	private final PaymentRepository paymentRepo;

	@Override
	public void markSuccess(PaymentEntity payment) {
		paymentRepo.save(payment);
	}

	@Override
	public Optional<PaymentEntity> findByContent(String content) {
		// TODO Auto-generated method stub
		return paymentRepo.findByContent(content);
	}

}
