package com.vjshow.marketplace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vjshow.marketplace.entity.PaymentEntity;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {

}
