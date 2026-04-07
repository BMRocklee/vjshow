package com.vjshow.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.PaymentMethodEntity;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {
}
