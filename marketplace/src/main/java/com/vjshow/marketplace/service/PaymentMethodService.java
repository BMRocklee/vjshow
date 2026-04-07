package com.vjshow.marketplace.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.vjshow.marketplace.dto.request.CreatePaymentMethodRequest;
import com.vjshow.marketplace.entity.CreatorEntity;
import com.vjshow.marketplace.entity.PaymentMethodEntity;
import com.vjshow.marketplace.repository.PaymentMethodRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    private final PaymentMethodRepository repo;

    @Transactional
    public void create(CreatorEntity creator, CreatePaymentMethodRequest req) {

        PaymentMethodEntity entity = PaymentMethodEntity.builder()
                .creator(creator)
                .name(req.getName())
                .bankName(req.getBankName())
                .accountNumber(req.getAccountNumber())
                .isDefault(false)
                .createdAt(LocalDateTime.now())
                .build();

        repo.save(entity);
    }
}
